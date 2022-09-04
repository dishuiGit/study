package cn.com.ptpress.cdm.spnego;

import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.jdbc.JdbcMeta;
import org.apache.calcite.avatica.remote.Driver;
import org.apache.calcite.avatica.remote.LocalService;
import org.apache.calcite.avatica.server.HttpServer;
import org.apache.calcite.avatica.util.DateTimeUtils;
import org.apache.kerby.kerberos.kerb.KrbException;
import org.apache.kerby.kerberos.kerb.client.KrbConfig;
import org.apache.kerby.kerberos.kerb.client.KrbConfigKey;
import org.apache.kerby.kerberos.kerb.server.SimpleKdcServer;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class SpnegoTest {

    protected static final String KEYSTORE_PASSWORD = "avaticasecret";
    protected static final List<HttpServer> SERVERS_TO_STOP = new ArrayList<>();

    protected static final String TARGET_DIR_NAME = System.getProperty("target.dir", "target");
    protected static final File TARGET_DIR =
            new File(System.getProperty("user.dir"), TARGET_DIR_NAME);
    protected static final File KEYSTORE = new File(TARGET_DIR, "avatica-test.jks");
    protected static LocalService localService;

    protected static String jdbcUrl = null;

    private static SimpleKdcServer kdc;

    private static int kdcPort;
    private static File clientKeytab;
    private static File serverKeytab;

    private static boolean isKdcStarted = false;

    public static void setupClass() throws SQLException {
        // Create a self-signed cert
        File target = SpnegoTestUtil.TARGET_DIR;
        File keystore = new File(target, "avatica-test.jks");
        if (keystore.isFile()) {
            assertTrue(keystore.delete(), "Failed to delete keystore: " + keystore);
        }
        new CertTool().createSelfSignedCert(keystore, "avatica", KEYSTORE_PASSWORD);

        // Create a LocalService
        String url = "jdbc:mysql://localhost:3306/db_cdm";
        final JdbcMeta meta = new JdbcMeta(url, "root", "root");
        localService = new LocalService(meta);
    }

    private static void setupKdc() throws Exception {
        if (isKdcStarted) {
            return;
        }
        if (System.getProperty("avatica.http.spnego.use_canonical_hostname") == null) {
            System.setProperty("avatica.http.spnego.use_canonical_hostname", "false");
        }
        kdc = new SimpleKdcServer();
        File target = SpnegoTestUtil.TARGET_DIR;
        assertTrue(target.exists());

        File kdcDir = new File(target, SpnegoTest.class.getSimpleName());
        if (kdcDir.exists()) {
            SpnegoTestUtil.deleteRecursively(kdcDir);
        }
        kdcDir.mkdirs();
        kdc.setWorkDir(kdcDir);

        kdc.setKdcHost(SpnegoTestUtil.KDC_HOST);
        kdcPort = SpnegoTestUtil.getFreePort();
        kdc.setAllowTcp(true);
        kdc.setAllowUdp(false);
        kdc.setKdcTcpPort(kdcPort);

        log.info("Starting KDC server at {}:{}", SpnegoTestUtil.KDC_HOST, kdcPort);

        kdc.init();
        kdc.start();
        isKdcStarted = true;

        File keytabDir = new File(target, SpnegoTest.class.getSimpleName()
                + "_keytabs");
        if (keytabDir.exists()) {
            SpnegoTestUtil.deleteRecursively(keytabDir);
        }
        keytabDir.mkdirs();
        setupServerUser(keytabDir);

        KrbConfig clientConfig = new KrbConfig();
        clientConfig.setString(KrbConfigKey.KDC_HOST, SpnegoTestUtil.KDC_HOST);
        clientConfig.setInt(KrbConfigKey.KDC_TCP_PORT, kdcPort);
        clientConfig.setString(KrbConfigKey.DEFAULT_REALM, SpnegoTestUtil.REALM);

        // Kerby sets "java.security.krb5.conf" for us!
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        //System.setProperty("sun.security.spnego.debug", "true");
        //System.setProperty("sun.security.krb5.debug", "true");
    }

    public static void stopServers() {
        for (HttpServer server : SERVERS_TO_STOP) {
            server.stop();
        }
        SERVERS_TO_STOP.clear();
    }

    @BeforeAll
    public static void init() throws Exception {
        setupClass();
        // Start the KDC
        setupKdc();

        jdbcUrl = getJdbcUrl(false, Driver.Serialization.PROTOBUF);
    }

    @AfterAll
    public static void stopKdc() throws KrbException {
        if (isKdcStarted) {
            log.info("Stopping KDC on {}", kdcPort);
            kdc.stop();
        }
        stopServers();
    }

    private static void setupServerUser(File keytabDir) throws KrbException {
        // Create the client user
        String clientPrincipal = SpnegoTestUtil.CLIENT_PRINCIPAL.substring(0,
                SpnegoTestUtil.CLIENT_PRINCIPAL.indexOf('@'));
        clientKeytab = new File(keytabDir, clientPrincipal.replace('/', '_') + ".keytab");
        if (clientKeytab.exists()) {
            SpnegoTestUtil.deleteRecursively(clientKeytab);
        }
        log.info("Creating {} with keytab {}", clientPrincipal, clientKeytab);
        SpnegoTestUtil.setupUser(kdc, clientKeytab, clientPrincipal);

        // Create the server user
        String serverPrincipal = SpnegoTestUtil.SERVER_PRINCIPAL.substring(0,
                SpnegoTestUtil.SERVER_PRINCIPAL.indexOf('@'));
        serverKeytab = new File(keytabDir, serverPrincipal.replace('/', '_') + ".keytab");
        if (serverKeytab.exists()) {
            SpnegoTestUtil.deleteRecursively(serverKeytab);
        }
        log.info("Creating {} with keytab {}", SpnegoTestUtil.SERVER_PRINCIPAL, serverKeytab);
        SpnegoTestUtil.setupUser(kdc, serverKeytab, SpnegoTestUtil.SERVER_PRINCIPAL);
    }

    public static List<Object[]> parameters() throws Exception {
        final ArrayList<Object[]> parameters = new ArrayList<>();

        setupClass();

        // Start the KDC
        setupKdc();

        for (boolean tls : new Boolean[]{false, true}) {
            for (org.apache.calcite.avatica.remote.Driver.Serialization serialization :
                    new org.apache.calcite.avatica.remote.Driver.Serialization[]{
                            org.apache.calcite.avatica.remote.Driver.Serialization.JSON,
                            Driver.Serialization.PROTOBUF}) {
                if (tls && System.getProperty("java.vendor").contains("IBM")) {
                    // Skip TLS testing on IBM Java due the combination of:
                    // - Jetty 9.4.12+ ignores SSL_* ciphers due to security - eclipse/jetty.project#2807
                    // - IBM uses SSL_* cipher names for ALL ciphers not following RFC cipher names
                    //   See eclipse/jetty.project#2807 for details
                    log.info("Skipping HTTPS test on IBM Java");
                    parameters.add(new Object[]{null});
                    continue;
                }
                String url = getJdbcUrl(tls, serialization);

                parameters.add(new Object[]{url});
            }
        }

        return parameters;
    }

    /**
     * Utility class for creating certificates for testing.
     */
    private static class CertTool {
        private static final String SIGNING_ALGORITHM = "SHA256WITHRSA";
        private static final String ENC_ALGORITHM = "RSA";

        static {
            Security.addProvider(new BouncyCastleProvider());
        }

        private void createSelfSignedCert(File targetKeystore, String keyName,
                                          String keystorePassword) {
            if (targetKeystore.exists()) {
                throw new RuntimeException("Keystore already exists: " + targetKeystore);
            }

            try {
                KeyPair kp = generateKeyPair();

                X509Certificate cert = generateCert(keyName, kp, true, kp.getPublic(),
                        kp.getPrivate());

                char[] password = keystorePassword.toCharArray();
                KeyStore keystore = KeyStore.getInstance("JKS");
                keystore.load(null, null);
                keystore.setCertificateEntry(keyName + "Cert", cert);
                keystore.setKeyEntry(keyName + "Key", kp.getPrivate(), password, new Certificate[]{cert});
                try (FileOutputStream fos = new FileOutputStream(targetKeystore)) {
                    keystore.store(fos, password);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
            KeyPairGenerator gen = KeyPairGenerator.getInstance(ENC_ALGORITHM);
            gen.initialize(2048);
            return gen.generateKeyPair();
        }

        private X509Certificate generateCert(String keyName, KeyPair kp, boolean isCertAuthority,
                                             PublicKey signerPublicKey, PrivateKey signerPrivateKey)
                throws IOException, OperatorCreationException, CertificateException,
                NoSuchAlgorithmException {
            Calendar startDate = DateTimeUtils.calendar();
            Calendar endDate = DateTimeUtils.calendar();
            endDate.add(Calendar.YEAR, 100);

            BigInteger serialNumber = BigInteger.valueOf(startDate.getTimeInMillis());
            X500Name issuer = new X500Name(
                    IETFUtils.rDNsFromString("cn=localhost", RFC4519Style.INSTANCE));
            JcaX509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuer,
                    serialNumber, startDate.getTime(), endDate.getTime(), issuer, kp.getPublic());
            JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
            certGen.addExtension(Extension.subjectKeyIdentifier, false,
                    extensionUtils.createSubjectKeyIdentifier(kp.getPublic()));
            certGen.addExtension(Extension.basicConstraints, false,
                    new BasicConstraints(isCertAuthority));
            certGen.addExtension(Extension.authorityKeyIdentifier, false,
                    extensionUtils.createAuthorityKeyIdentifier(signerPublicKey));
            if (isCertAuthority) {
                certGen.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign));
            }
            X509CertificateHolder certificateHolder = certGen.build(
                    new JcaContentSignerBuilder(SIGNING_ALGORITHM).build(signerPrivateKey));
            return new JcaX509CertificateConverter().getCertificate(certificateHolder);
        }
    }

    private static String getJdbcUrl(boolean tls, Driver.Serialization serialization) {
        // Build and start the server
        HttpServer.Builder httpServerBuilder = new HttpServer.Builder();
        if (tls) {
            httpServerBuilder = httpServerBuilder
                    .withTLS(KEYSTORE, KEYSTORE_PASSWORD, KEYSTORE, KEYSTORE_PASSWORD);
        }
        HttpServer httpServer = httpServerBuilder
                .withPort(0)
                .withAutomaticLogin(serverKeytab)
                .withSpnego(SpnegoTestUtil.SERVER_PRINCIPAL, SpnegoTestUtil.REALM)
                .withHandler(localService, serialization)
                .build();
        httpServer.start();
        SERVERS_TO_STOP.add(httpServer);

        String url = "jdbc:avatica:remote:url=" + (tls ? "https://" : "http://")
                + SpnegoTestUtil.KDC_HOST + ":" + httpServer.getPort()
                + ";authentication=SPNEGO;serialization=" + serialization;
        if (tls) {
            url += ";truststore=" + KEYSTORE.getAbsolutePath()
                    + ";truststore_password=" + KEYSTORE_PASSWORD;
        }
        // jdbc:avatica:remote:url=http://localhost:50464;authentication=SPNEGO;serialization=PROTOBUF
        log.info("JDBC URL {}", url);
        return url;
    }

    @Test
    void spnegoAuthTest() throws SQLException {
        final String tableName = "automaticAllowedClients";
        String url = jdbcUrl + ";principal=" + SpnegoTestUtil.CLIENT_PRINCIPAL + ";keytab="
                + clientKeytab;
        /*
        jdbc:avatica:remote:url=http://localhost:50464;authentication=SPNEGO;serialization=PROTOBUF;principal
        =client@EXAMPLE.COM;keytab=D:\xxx\target\SpnegoTest_keytabs\client.keytab
         */
        log.info("Updated JDBC url: {}", url);
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            assertFalse(stmt.execute("DROP TABLE IF EXISTS " + tableName));
            assertFalse(stmt.execute("CREATE TABLE " + tableName + "(pk integer)"));
            assertEquals(1, stmt.executeUpdate("INSERT INTO " + tableName + " VALUES(1)"));
            assertEquals(1, stmt.executeUpdate("INSERT INTO " + tableName + " VALUES(2)"));
            assertEquals(1, stmt.executeUpdate("INSERT INTO " + tableName + " VALUES(3)"));

            ResultSet results = stmt.executeQuery("SELECT count(1) FROM " + tableName);
            assertTrue(results.next());
            assertEquals(3, results.getInt(1));
        }
    }
}
