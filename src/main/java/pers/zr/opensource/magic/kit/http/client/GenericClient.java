package pers.zr.opensource.magic.kit.http.client;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zr.opensource.magic.kit.http.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GenericClient {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected abstract CloseableHttpClient createHttpClient() throws MagicHttpException;


    public MagicHttpResponse send(MagicHttpRequest magicHttpRequest) throws MagicHttpException {

        MagicHttpResponse magicHttpResponse;
        switch (magicHttpRequest.getMethod()) {
            case GET:
                magicHttpResponse = sendGet(magicHttpRequest);
                break;
            case POST:
                magicHttpResponse = sendPost(magicHttpRequest);
                break;
            case PUT:
                magicHttpResponse = sendPut(magicHttpRequest);
                break;
            case PATCH:
                magicHttpResponse = sendPatch(magicHttpRequest);
                break;
            case DELETE:
                magicHttpResponse = sendDelete(magicHttpRequest);
                break;
            default:
                throw new MagicHttpException("invalid request method type!");

        }

        return magicHttpResponse;

    }


    private MagicHttpResponse sendGet(MagicHttpRequest magicHttpRequest) throws MagicHttpException {

        HttpGet httpGet = new HttpGet(getRequestEncodedUrl(magicHttpRequest));

        //设置header
        setHeaders(httpGet, magicHttpRequest);
        //设置请求超时时间
        setTimeout(httpGet, magicHttpRequest);

        return executeForBytes(httpGet);
    }

    private MagicHttpResponse sendDelete(MagicHttpRequest magicHttpRequest) throws MagicHttpException {

        HttpDelete httpDelete = new HttpDelete(getRequestEncodedUrl(magicHttpRequest));
        //设置header
        setHeaders(httpDelete, magicHttpRequest);
        //设置请求超时时间
        setTimeout(httpDelete, magicHttpRequest);

        return executeForBytes(httpDelete);
    }

    private MagicHttpResponse sendPost(MagicHttpRequest magicHttpRequest) throws MagicHttpException {

        HttpPost httpPost = new HttpPost(magicHttpRequest.getUri());
        //设置请求body
        HttpEntity body = getRequestEncodedBody(magicHttpRequest);
        httpPost.setEntity(body);
        //设置请求headers
        setHeaders(httpPost, magicHttpRequest);
        //设置请求超时时间
        setTimeout(httpPost, magicHttpRequest);

        return executeForBytes(httpPost);

    }

    private MagicHttpResponse sendPut(MagicHttpRequest magicHttpRequest) throws MagicHttpException {

        HttpPut httpPut = new HttpPut(magicHttpRequest.getUri());
        //设置请求body
        HttpEntity body = getRequestEncodedBody(magicHttpRequest);
        httpPut.setEntity(body);
        //设置请求headers
        setHeaders(httpPut, magicHttpRequest);
        //设置请求超时时间
        setTimeout(httpPut, magicHttpRequest);

        return executeForBytes(httpPut);

    }

    private MagicHttpResponse sendPatch(MagicHttpRequest magicHttpRequest) throws MagicHttpException {

        HttpPatch httpPatch = new HttpPatch(magicHttpRequest.getUri());
        //设置请求body
        HttpEntity body = getRequestEncodedBody(magicHttpRequest);
        httpPatch.setEntity(body);
        //设置请求headers
        setHeaders(httpPatch, magicHttpRequest);
        //设置请求超时时间
        setTimeout(httpPatch, magicHttpRequest);

        return executeForBytes(httpPatch);

    }

    private String getRequestEncodedUrl(MagicHttpRequest magicHttpRequest) {

        StringBuilder finalUrl = new StringBuilder(magicHttpRequest.getUri());
        Map<?,?> params = magicHttpRequest.getParams();
        if(params != null && !params.isEmpty()) {
            List<NameValuePair> nvpList = params.entrySet()
                    .stream().filter(entry -> entry.getKey() != null && entry.getValue() != null)
                    .map(entry -> new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()))
                    .collect(Collectors.toList());
            String encodedParamString = URLEncodedUtils.format(nvpList, Charset.defaultCharset());
            if(null != encodedParamString) {
                finalUrl.append("?").append(encodedParamString);
            }
        }
        return finalUrl.toString();
    }

    private HttpEntity getRequestEncodedBody(MagicHttpRequest magicHttpRequest) throws MagicHttpException{

        HttpEntity requestEntity;
        if(RequestContentType.APPLICATION_FORM_URLENCODED.equals(magicHttpRequest.getContentType())) {
            requestEntity = getEncodedFormEntity(magicHttpRequest.getParams());
        }else if(RequestContentType.APPLICATION_JSON.equals(magicHttpRequest.getContentType())) {
            requestEntity = getEncodedJsonEntity(magicHttpRequest.getParams());
        }else {
            throw new MagicHttpException("content type[" + magicHttpRequest.getContentType() + "] is not support!");
        }
        return requestEntity;
    }

    private UrlEncodedFormEntity getEncodedFormEntity(Map<?,?> params) {
        UrlEncodedFormEntity encodedFormEntity = null;
        if(params != null && !params.isEmpty()) {
            List<NameValuePair> nvpList = params.entrySet().stream()
                    .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                    .map(entry -> new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()))
                    .collect(Collectors.toList());
            encodedFormEntity = new UrlEncodedFormEntity(nvpList, Consts.UTF_8);
        }
        return encodedFormEntity;
    }

    private StringEntity getEncodedJsonEntity(Map<?,?> params) {
        return new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON);

    }

    private void setHeaders(HttpRequestBase httpOperation, MagicHttpRequest magicHttpRequest) {
        Map<String, String> headers = magicHttpRequest.getHeaders();
        if(headers != null && !headers.isEmpty()) {
            for(String key : headers.keySet()) {
                httpOperation.setHeader(key, headers.get(key));
            }
        }
    }

    private void setTimeout(HttpRequestBase httpOperation, MagicHttpRequest magicHttpRequest) {
        RequestTimeout timeout = magicHttpRequest.getTimeout();
        if(timeout != null) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout.getConnectionTimeout())
                    .setSocketTimeout(timeout.getSocketTimeout()).build();
            httpOperation.setConfig(requestConfig);
        }
    }

    private MagicHttpResponse executeForBytes(HttpUriRequest request) throws MagicHttpException {
        MagicHttpResponse magicHttpResponse;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpclient = createHttpClient();
            httpResponse = httpclient.execute(request);

            StatusLine statusLine = httpResponse.getStatusLine();
            magicHttpResponse = new MagicHttpResponse(statusLine.getStatusCode());

            HttpEntity entity = httpResponse.getEntity();
            if(entity != null) {
                InputStream inputStream = entity.getContent();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int count;
                try {
                    while((count = inputStream.read(data,0,1024)) != -1) {
                        outStream.write(data, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                magicHttpResponse.setBody(outStream.toByteArray());
            }
            return magicHttpResponse;

        } catch (IOException e) {
            throw new MagicHttpException(e);

        }finally {
            closeResponse(httpResponse);
            closeClient(httpclient);
        }
    }


    private void closeResponse(CloseableHttpResponse httpResponse) {
        if(httpResponse != null) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void closeClient(CloseableHttpClient httpClient) {
        if(httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
