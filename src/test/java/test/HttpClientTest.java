package test;

import pers.zr.opensource.magic.kit.http.client.MagicCertificateHttpsClient;

/**
 * Created by zhurong on 2016-6-17.
 */
public class HttpClientTest {

    public static void main(String [] args) {
        for(int i=0; i<1000; i++) {
            System.out.println(MagicCertificateHttpsClient.getInstance(null, null));
        }
    }
}
