package com.gridmancer.example.undertow.vuejs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

class KeyStoreHelper {
  public static char[] SECRET = "secret".toCharArray();

  private KeyStoreHelper() {
    // prevent instantiation
  }

  public static KeyManager[] getKeyManagers(
      final String certificatePath, final String privateKeyPath)
      throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException,
          UnrecoverableKeyException {
    final KeyStore keyStore = createKeyStore(certificatePath, privateKeyPath);
    final KeyManagerFactory keyManagerFactory =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, SECRET);
    return keyManagerFactory.getKeyManagers();
  }

  public static KeyStore createKeyStore(final String certificatePath, final String privateKeyPath)
      throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
    final CertificateFactory cf = CertificateFactory.getInstance("X.509");
    final JcaPEMKeyConverter pemConverter = new JcaPEMKeyConverter();

    X509Certificate certificate;
    PrivateKey privateKey;

    try (PEMParser pemParser = new PEMParser(Files.newBufferedReader(Paths.get(privateKeyPath)))) {
      PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
      privateKey = pemConverter.getPrivateKey(privateKeyInfo);
    }

    certificate =
        (X509Certificate) cf.generateCertificate(Files.newInputStream(Paths.get(certificatePath)));
    KeyStore keyStore = KeyStore.getInstance("JKS");
    String alias = certificate.getSubjectX500Principal().getName();
    keyStore.load(null);
    keyStore.setKeyEntry(alias, privateKey, SECRET, new X509Certificate[] {certificate});
    return keyStore;
  }
}
