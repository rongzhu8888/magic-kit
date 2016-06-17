package pers.zr.opensource.magic.kit.http;

import org.apache.http.Consts;

public class MagicHttpResponse {

    private int statusCode;
    private byte[] body;

    public MagicHttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getBodyAsString() {
        return new String(body, Consts.UTF_8);
    }
}
