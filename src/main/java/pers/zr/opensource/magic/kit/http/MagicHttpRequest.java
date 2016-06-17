package pers.zr.opensource.magic.kit.http;

import java.util.Map;

/**
 * Created by peter.zhu on 2016/1/18.
 *
 */
public class MagicHttpRequest {

    private String uri;  //请求URL

    private Map<?, ?> params;   //请求内容

    private Map<String, String> headers;  //请求头信息

    private RequestContentType contentType; //请求内容格式

    private RequestMethodType method;    //请求类型

    private RequestTimeout timeout;  //请求超时

    public MagicHttpRequest(String uri) {
        this.uri = uri;
        this.method = RequestMethodType.GET;
    }

    public MagicHttpRequest(String uri, RequestMethodType method) {
        this.uri = uri;
        this.method = method;
    }

    public MagicHttpRequest(String uri, RequestMethodType method, RequestTimeout timeout) {
        this.uri = uri;
        this.method = method;
        this.timeout = timeout;
    }

    public MagicHttpRequest(String uri, RequestMethodType method, RequestContentType contentType) {
        this.uri = uri;
        this.method = method;
        this.contentType = contentType;
    }

    public MagicHttpRequest(String uri, RequestMethodType method, RequestContentType contentType, RequestTimeout timeout) {
        this.uri = uri;
        this.method = method;
        this.contentType = contentType;
        this.timeout = timeout;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<?, ?> getParams() {
        return params;
    }

    public void setParams(Map<?, ?> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public RequestContentType getContentType() {
        return contentType;
    }

    public void setContentType(RequestContentType contentType) {
        this.contentType = contentType;
    }

    public void setMethod(RequestMethodType method) {
        this.method = method;
    }

    public RequestMethodType getMethod() {
        return method;
    }

    public RequestTimeout getTimeout() {
        return timeout;
    }

    public void setTimeout(RequestTimeout timeout) {
        this.timeout = timeout;
    }
}
