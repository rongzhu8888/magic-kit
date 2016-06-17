package pers.zr.opensource.magic.kit.http.client;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import pers.zr.opensource.magic.kit.http.MagicHttpException;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 信任所有的HTTPS请求
 */
public class MagicTrustedHttpsClient extends GenericClient {

    private MagicTrustedHttpsClient(){}

    private static class InnerClass {
        private static final MagicTrustedHttpsClient instance = new MagicTrustedHttpsClient();
    }

    public static MagicTrustedHttpsClient getInstance() {
        return InnerClass.instance;
    }

    @Override
    protected CloseableHttpClient createHttpClient() throws MagicHttpException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true; //信任所有
                }

            }).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new MagicHttpException(e);
        }
    }


}
