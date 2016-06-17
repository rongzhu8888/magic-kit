package pers.zr.opensource.magic.kit.http.client;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import pers.zr.opensource.magic.kit.http.MagicHttpException;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * 加载keystore证书的Https请求
 * Created by zhurong on 2016-6-16.
 */
public class MagicCertificateHttpsClient extends GenericClient {

    private String keystorePassword = null;
    private File keystore = null;

    private MagicCertificateHttpsClient() {}

    private static MagicCertificateHttpsClient instance;

    public static MagicCertificateHttpsClient getInstance(File keystore, String keystorePassword) {
        instance = InnerClass.instance;
        instance.keystore = keystore;
        instance.keystorePassword = keystorePassword;
        return instance;
    }


    private static class InnerClass {
        private static final MagicCertificateHttpsClient instance = new MagicCertificateHttpsClient();
    }

    @Override
    protected CloseableHttpClient createHttpClient() throws MagicHttpException {

        // Trust own CA and all self-signed certs
        CloseableHttpClient httpsClient;
        SSLContext sslcontext ;
        try {
            sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(this.keystore, (this.keystorePassword != null? this.keystorePassword.toCharArray() : null),
                            new TrustSelfSignedStrategy()).build();

            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            httpsClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException | IOException e) {
            throw new MagicHttpException(e);
        }

        return httpsClient;
    }
}
