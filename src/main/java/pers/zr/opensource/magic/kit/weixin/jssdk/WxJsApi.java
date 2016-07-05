package pers.zr.opensource.magic.kit.weixin.jssdk;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zr.opensource.magic.kit.cipher.Coder;
import pers.zr.opensource.magic.kit.http.MagicHttpException;
import pers.zr.opensource.magic.kit.http.MagicHttpRequest;
import pers.zr.opensource.magic.kit.http.MagicHttpResponse;
import pers.zr.opensource.magic.kit.http.RequestMethodType;
import pers.zr.opensource.magic.kit.http.client.MagicTrustedHttpsClient;
import pers.zr.opensource.magic.kit.weixin.WxApiException;
import pers.zr.opensource.magic.kit.weixin.WxResponseValidator;


import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

/**
 * 微信公众号JsApi
 */
public class WxJsApi {

    private static final String JSTICKET_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/ticket/getticket" +
            "?access_token=[access_token]&type=jsapi";

    private static final Logger log = LoggerFactory.getLogger(WxJsApi.class);
    
    /**
     * 获取JS Ticket
     * 注意：Ticket有效期为7200秒，并且微信获取该ticket具有频次限制，
     * 所以业务代码必须将该Ticket缓存起来，并定时刷新
     * @param commonAccessToken 通用access token
     * @return JS Ticket
     * @throws WxApiException
     */
    public static String getJsApiTicket(String commonAccessToken) throws WxApiException {

        try{
            String ticketUrl = JSTICKET_URL_TEMPLATE.replace("[access_token]", commonAccessToken);
            MagicHttpRequest request = new MagicHttpRequest(ticketUrl, RequestMethodType.GET);
            MagicHttpResponse ticketResponse = MagicTrustedHttpsClient.getInstance().send(request);
            if(log.isDebugEnabled()) {
                log.debug("JsApiTicket Response: " + ticketResponse.getBodyAsString());

            }
            if(ticketResponse.getStatusCode() != 200) {
                throw new WxApiException("weixin [GET_JS_TICKET] api response error: " +
                        "status_code = [" + ticketResponse.getStatusCode() + "]," +
                        "error_message = [" + ticketResponse.getBodyAsString() + "]");
            }
            Map ticketMap = JSON.parseObject(ticketResponse.getBodyAsString(), Map.class);
            if(WxResponseValidator.isErrorOccured(ticketMap)) {
                throw new WxApiException("weixin [GET_JS_TICKET] api response error: " +
                        "status_code = [200], " +
                        "error_message = [" + ticketMap.get("errmsg").toString() + "]");
            }else {
                return ticketMap.get("ticket").toString();
            }
        }catch (MagicHttpException e) {
            throw new WxApiException("Failed to get jsApiTicket, caused by " + e.getMessage());
        }

    }

    /**
     * 获取JSAPI签名
     * -------------------------------------------------------------------------------------------
     * 对jsapi_ticket、 timestamp 和 nonce 按字典排序 对所有待签名参数按照字段名的 ASCII
     * 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
     * string1。这里需要注意的是所有参数名均为小写字符。 接下来对 string1 作 sha1 加密，字段名和字段值
     * 都采用原始值，不进行URL 转义。即 signature=sha1(string1)。
     * -------------------------------------------------------------------------------------------
     * @param jsTicket ticket
     * @param timestamp 当前时间戳（毫秒）
     * @param nonce 随即串
     * @param url 当前网页URL，不包含#及其后面部分
     * @return JSAPI签名
     * @throws Exception
     */
    public static String getJsApiSignature(String jsTicket, String timestamp, String nonce, String url) throws WxApiException {

        try{
            String[] paramArr = new String[] { "jsapi_ticket=" + jsTicket,"timestamp=" + timestamp, "noncestr=" + nonce, "url=" + url };
            Arrays.sort(paramArr);
            // 将排序后的结果拼接成一个字符串
            String content = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2]).concat("&" + paramArr[3]);
            MessageDigest md = MessageDigest.getInstance("SHA1");
            // 对拼接后的字符串进行 sha1 加密
            byte[] digest = md.digest(content.getBytes());
            String genSignature = Coder.parseBytes2HexStr(digest, false);
            if(log.isDebugEnabled()) {
                log.debug("Js Api Signature = [" + genSignature + "], params=[" + paramArr.toString() + "]");

            }
            return genSignature;
        }catch (Exception e) {
            throw new WxApiException("Failed to get JsApiSignature, caused by " + e.getMessage());
        }



    }


}
