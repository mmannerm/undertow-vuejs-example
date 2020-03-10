package com.gridmancer.example.undertow.vuejs;

import java.net.InetSocketAddress;

import javax.annotation.concurrent.NotThreadSafe;
import javax.net.ssl.SSLContext;
import javax.servlet.ServletException;
import javax.ws.rs.core.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.core.ResteasyDeploymentImpl;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.xnio.Options;
import org.xnio.Sequence;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.RequestLimit;
import io.undertow.server.handlers.RequestLimitingHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

@NotThreadSafe
public class Server {
    private static final ClassLoader CLASSLOADER = Server.class.getClassLoader();
    private static final Sequence<String> TLS_PROTOCOLS = Sequence.of("TLSv1.2", "TLSv1.3");

    private static final Logger log = LogManager.getLogger();

    private Undertow instance;
    private int port;
    private String publicKeyPath;
    private String privateKeyPath;

    // This is exposed only for unit testing purposes instead of local variable in
    // main()
    static Server mainServer;

    public Server(final int port) {
        this.port = port;
        this.publicKeyPath = "src/test/resources/certificate.pem";
        this.privateKeyPath = "src/test/resources/key.pem";
    }

    public void stop() {
        if (instance != null) {
            instance.stop();
            instance = null;
        }
    }

    public boolean isRunning() {
        return instance != null;
    }

    public int getPort() {
        return port;
    }

    public static void main(final String[] args) throws Exception {
        mainServer = new Server(0);
        mainServer.start();
    }

    public void start() throws Exception {

        log.info("publicKeyPath={}", publicKeyPath);
        log.info("privateKeyPath={}", privateKeyPath);

        // http://undertow.io/undertow-docs/undertow-docs-2.0.0/index.html#request-limiting-handler
        final RequestLimitingHandler requestLimitingHandler = new RequestLimitingHandler(new RequestLimit(150, 300),
                Handlers.path().addPrefixPath("/", getServletHandler(new RestApplication())));
        final HttpHandler accessLogHandler = new AccessLogHandler(requestLimitingHandler, new BasicLogReceiver(),
                "combined", CLASSLOADER);

        final SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(KeyStoreHelper.getKeyManagers(publicKeyPath, privateKeyPath), null, null);

        final Undertow.Builder builder = Undertow.builder()
                .addListener(new Undertow.ListenerBuilder().setType(Undertow.ListenerType.HTTPS).setHost("0.0.0.0")
                        .setPort(port).setSslContext(sslContext))
                .setSocketOption(Options.CONNECTION_HIGH_WATER, 1500)
                .setSocketOption(Options.CONNECTION_LOW_WATER, 1200).setSocketOption(Options.BACKLOG, 20)
                .setSocketOption(Options.SSL_ENABLED_PROTOCOLS, TLS_PROTOCOLS)
                .setSocketOption(UndertowOptions.SSL_USER_CIPHER_SUITES_ORDER, true)
                .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true)
                .setIoThreads(Math.max(1, Runtime.getRuntime().availableProcessors() - 1)).setHandler(accessLogHandler)
                .setWorkerThreads(150);

        instance = builder.build();
        instance.start();

        for (final Undertow.ListenerInfo listener : instance.getListenerInfo()) {
            log.info("protocol={} address={}", listener.getProtcol(), listener.getAddress());
            port = ((InetSocketAddress) listener.getAddress()).getPort();
        }
    }

    private HttpHandler getServletHandler(Application app) throws ServletException {
        final ResteasyDeployment deployment = new ResteasyDeploymentImpl();
        deployment.setApplication(app);

        final DeploymentInfo servletBuilder = Servlets.deployment().setClassLoader(CLASSLOADER).setContextPath("/")
                .setDeploymentName("RestEasy")
                .addServlets(Servlets.servlet("ResteasyServlet", HttpServlet30Dispatcher.class).setAsyncSupported(true)
                        .setLoadOnStartup(1).addMapping("/"))
                .addServletContextAttribute(ResteasyDeployment.class.getName(), deployment);

        final DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);

        manager.deploy();
        return manager.start();
    }

    static class BasicLogReceiver implements AccessLogReceiver {
        private final Logger ACCESS_LOGGER = LogManager.getLogger("accessLog");
        private final int maxLogMessages;
        private volatile int logMessageCount = 0;

        public BasicLogReceiver(final int maxLogMessages) {
            this.maxLogMessages = maxLogMessages;
        }

        public BasicLogReceiver() {
            this(-1);
        }

        @Override
        public void logMessage(final String message) {
            if (maxLogMessages > 0 && logMessageCount >= maxLogMessages) {
                return;
            }
            ACCESS_LOGGER.info(message);

            if (maxLogMessages > 0) {
                logMessageCount++;
            }
        }
    }
}
