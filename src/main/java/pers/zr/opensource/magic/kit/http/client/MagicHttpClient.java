package pers.zr.opensource.magic.kit.http.client;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import pers.zr.opensource.magic.kit.http.MagicHttpException;


/**
 * HTTP请求
 */
public class MagicHttpClient extends GenericClient {

    private MagicHttpClient() {}

    private static class InnerClass {
        private static final MagicHttpClient instance = new MagicHttpClient();
    }

    public static MagicHttpClient getInstance() {
        return InnerClass.instance;
    }

    @Override
    protected CloseableHttpClient createHttpClient() throws MagicHttpException {
        return HttpClients.createDefault();
    }
}
