package com.gridmancer.example.undertow.vuejs;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

class TestHelper {
  private TestHelper() {
    // prevent instantiation
  }

  public static CloseableHttpClient createUnsecureTestClient()
      throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
    return HttpClients.custom()
        .setSSLContext(
            new SSLContextBuilder().loadTrustMaterial(TrustSelfSignedStrategy.INSTANCE).build())
        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
        .build();
  }
}
