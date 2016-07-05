package pers.zr.opensource.magic.kit.weixin.common;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zr.opensource.magic.kit.http.MagicHttpException;
import pers.zr.opensource.magic.kit.http.MagicHttpRequest;
import pers.zr.opensource.magic.kit.http.MagicHttpResponse;
import pers.zr.opensource.magic.kit.http.RequestMethodType;
import pers.zr.opensource.magic.kit.http.client.MagicTrustedHttpsClient;
import pers.zr.opensource.magic.kit.weixin.WxApiException;
import pers.zr.opensource.magic.kit.weixin.WxResponseValidator;


import java.util.Map;

/**
 * 微信公众号公用API
 */
public class WxCommonApi {

    private static final String COMMON_ACCESS_TOKEN_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/token?" +
            "grant_type=client_credential&appid=[appid]&secret=[secret]";

    private static final Logger log = LoggerFactory.getLogger(WxCommonApi.class);

    /**
     * 获取通用ACCESS TOKEN
     * 注意：TOKEN有效期为7200秒，并且微信接口具有频次限制，
     * 所以业务代码必须将该TOKEN缓存起来，并定时刷新
     * @param appId 公众号appId
     * @param secret 公众号secret
     * @return 通用ACCESS TOKEN
     * @throws Exception
     */
    public static String getCommonAccessToken(String appId, String secret) throws WxApiException {
        try{
            //TODO 优先从缓存中获取

            String tokenUrl = COMMON_ACCESS_TOKEN_URL_TEMPLATE.
                    replace("[appid]", appId).replace("[secret]", secret);
            MagicHttpRequest request = new MagicHttpRequest(tokenUrl, RequestMethodType.GET);
            MagicHttpResponse tokenResponse = MagicTrustedHttpsClient.getInstance().send(request);
            if(log.isDebugEnabled()) {
                log.debug("CommonAccessToken Response: " + tokenResponse.getBodyAsString());

            }
            if(tokenResponse.getStatusCode() != 200) {
                throw new WxApiException("weixin [GET_COMMON_ACCESS_TOKEN] api response error: " +
                        "status_code = [" + tokenResponse.getStatusCode() + "], " +
                        "error_message = [" + tokenResponse.getBodyAsString() + "]");
            }
            Map tokenMap = JSON.parseObject(tokenResponse.getBodyAsString(), Map.class);
            if(WxResponseValidator.isErrorOccured(tokenMap)) {
                throw new WxApiException("weixin [GET_COMMON_ACCESS_TOKEN] api response error: " +
                        "status_code = [200], " +
                        "error_message=[" + tokenMap.get("errmsg").toString() + "]");
            }else {
                return tokenMap.get("access_token").toString();
            }
        }catch (MagicHttpException e) {
            throw new WxApiException("Failed to get commonAccessToken, caused by " + e.getMessage());
        }
    }
}
