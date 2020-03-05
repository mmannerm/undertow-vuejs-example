/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.gridmancer.example.undertow.vuejs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import mockit.Mocked;
import mockit.Verifications;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ServerTest {
    private CloseableHttpClient client = HttpClients.createDefault();
    private CloseableHttpResponse response;
    private Server server;

    @AfterEach
    public void tearDown() {
        HttpClientUtils.closeQuietly(response);
        HttpClientUtils.closeQuietly(client);

        if (server != null) {
            if (server.isRunning()) {
                server.stop();
            }
            server = null;
        }
    }

    @Test
    public void testServerMain() throws Throwable {
        Server.main(new String[0]);
        assertNotNull(Server.mainServer);
        assertTrue(Server.mainServer.isRunning());

        Server.mainServer.stop();
        assertFalse(Server.mainServer.isRunning());

        // Server can be stopped twice
        Server.mainServer.stop();
        assertFalse(Server.mainServer.isRunning());
    }

    @Test
    public void testUnknownResource() throws Exception {
        server = new Server(0);
        server.start();

        HttpGet ping = new HttpGet("http://localhost:" + server.getPort() + "/thisendpointwillnotexist");

        try (CloseableHttpResponse response = client.execute(ping)) {
            assertEquals(404, response.getStatusLine().getStatusCode());
        }
    }

    @Test
    public void testAccessLogHandlerMaxLogCount(@Mocked Logger accessLogger, @Mocked LogManager logManager) {
        Server.BasicLogReceiver accessLogReceiver = new Server.BasicLogReceiver(1);
        accessLogReceiver.logMessage("foo1");
        accessLogReceiver.logMessage("foo2");

        new Verifications() {
            {
                accessLogger.info(anyString);
                times = 1;
            }
        };
    }

    @Test
    public void testAccessLogHandler(@Mocked Logger accessLogger, @Mocked LogManager logManager) {
        Server.BasicLogReceiver accessLogReceiver = new Server.BasicLogReceiver();
        accessLogReceiver.logMessage("foo1");
        accessLogReceiver.logMessage("foo2");

        new Verifications() {
            {
                accessLogger.info(anyString);
                times = 2;
            }
        };
    }
}
