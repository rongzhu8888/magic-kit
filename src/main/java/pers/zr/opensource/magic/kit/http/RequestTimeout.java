package pers.zr.opensource.magic.kit.http;

/**
 *
 * 超时
 *
 * Created by peter on 2016/2/5.
 *
 */
public class RequestTimeout {

    private int connectionTimeout; //与服务器建立连接的超时时间(单位：ms)

    private int socketTimeout; //socket读取数据的超时时间，即从服务器获取响应数据最长等待时间(单位：ms)

    public RequestTimeout(int connectionTimeout, int socketTimeout) {
        this.connectionTimeout = connectionTimeout;
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }
}
