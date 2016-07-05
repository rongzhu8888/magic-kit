package pers.zr.opensource.magic.kit.weixin.user;

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


import java.net.URLEncoder;
import java.util.Map;

/**
 * 微信公众号用户网页授权API
 *
 * #############################################################################
 *
 * 1.微信公众号配置网页授权回调域名（支持IP+端口）
 * 2.引导用户进入授权页面
 * 3.用户同意后在回调页面获取code参数，调用微信API获取access_token和openid
 * 4.根据access_token和openid调用微信API获取用户基本信息
 *
 * #############################################################################
 * @author zhurong
 */
public class WxOAuth2Api {

    private static final String OAUTH_URL_TEMPLATE =
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=[appid]" +
            "&redirect_uri=[redirect_uri]&response_type=code&scope=[scope]&state=STATE#wechat_redirect";

    private static final String ACCESS_TOKEN_URL_TEMPLATE =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=[appid]" +
            "&secret=[secret]&code=[code]&grant_type=authorization_code";

    private static final String USER_INFO_URL_TEMPLATE =
            "https://api.weixin.qq.com/sns/userinfo?access_token=[access_token]&openid=[openid]&lang=zh_CN";

    private static final Logger log = LoggerFactory.getLogger(WxOAuth2Api.class);

    /**
     * 获取静默授权引导页URL（无需用户手动点击授权，由微信直接跳转回调页面，只能获取用户openid）
     * @param appId 公众号appId
     * @param redirectUrl 回调页面（业务页面）
     * @return 静默授权引导页面URL
     */
    public static String getStaticOAuthUrl(String appId, String redirectUrl) throws WxApiException {
        return getOAuthUrl(appId, redirectUrl, OAuthType.STATIC);
    }

    /**
     * 获取用户手动授权引导页URL（用户手动授权后，由微信跳转回调页面,可以获取用户信息）
     * @param appId 公众号appId
     * @param redirectUrl 回调页面
     * @return 手动授权引导页面URL
     */
    public static String getManualOAuthUrl(String appId, String redirectUrl) throws WxApiException {
        return getOAuthUrl(appId, redirectUrl, OAuthType.MANUAL);
    }

    private static String getOAuthUrl(String appId, String redirectUrl, OAuthType oAuthType) throws WxApiException {

        if(OAuthType.STATIC != oAuthType && OAuthType.MANUAL != oAuthType) {
            throw new WxApiException("Failed to get OAuth url, caused by invalid oauth type[" + oAuthType + "]");
        }
        try{
            String oauthUrl = OAUTH_URL_TEMPLATE
                    .replace("[appid]", appId)
                    .replace("[redirect_uri]", URLEncoder.encode(redirectUrl, "UTF-8"))
                    .replace("[scope]", oAuthType.value);

            if(log.isDebugEnabled()) {
                String params = "appid=" + appId + ",redirectUrl=" + redirectUrl;
                log.debug("oauth url=[" + oauthUrl + "], params=[" + params + "]");
            }
            return oauthUrl;
        }catch (Exception e) {
            throw new WxApiException("Failed to get OAuth url, caused by " + e.getMessage());
        }

    }

    /**
     * 根据CODE获取用户基本信息
     * @param appId 微信APPID
     * @param secret 微信SECRET
     * @param code CODE
     * @return 用户信息
     */
    public static WxUser getUserInfo(String appId, String secret, String code) throws WxApiException {
        try{

            //获取access_token和openid接口
            String accessTokenUrl = ACCESS_TOKEN_URL_TEMPLATE.replace("[appid]", appId)
                    .replace("[secret]", secret).replace("[code]", code);
            MagicHttpRequest request = new MagicHttpRequest(accessTokenUrl, RequestMethodType.GET);
            MagicHttpResponse tokenResponse = MagicTrustedHttpsClient.getInstance().send(request);
            log.debug("oauth token response=" + tokenResponse.getBodyAsString());
            if(tokenResponse.getStatusCode() != 200) {
                throw new WxApiException("weixin [GET_OATH_TOKEN] api response error: " +
                        "status_code = [" + tokenResponse.getStatusCode() + "], " +
                        "error_message = [" + tokenResponse.getBodyAsString() + "]");
            }
            Map tokenMap = JSON.parseObject(tokenResponse.getBodyAsString(), Map.class);
            if(!WxResponseValidator.isErrorOccured(tokenMap)) {
                String accessToken = tokenMap.get("access_token").toString();
                String openId = tokenMap.get("openid").toString();

                //获取用户信息接口
                String userInfoUrl = USER_INFO_URL_TEMPLATE.replace("[access_token]", accessToken).replace("[openid]", openId);
                request.setUri(userInfoUrl);
                MagicHttpResponse userInfoResponse = MagicTrustedHttpsClient.getInstance().send(request);
                if(userInfoResponse.getStatusCode() != 200) {
                    throw new WxApiException("weixin [GET_USER_INFO] api response error: " +
                            "status_code = [" + userInfoResponse.getStatusCode() + "], " +
                            "error_message = [" + userInfoResponse.getBodyAsString() + "]");
                }
                log.debug("user info response=" + userInfoResponse.getBodyAsString());
                Map userMap = JSON.parseObject(userInfoResponse.getBodyAsString(), Map.class);
                if(!WxResponseValidator.isErrorOccured(userMap)) {
                    WxUser userInfo = new WxUser();
                    userInfo.setOpenid(userMap.get("openid").toString());
                    userInfo.setUnionid(userMap.get("unionid").toString());
                    userInfo.setNickname(userMap.get("nickname").toString());
                    userInfo.setSex(userMap.get("sex").toString());
                    userInfo.setAvatar(userMap.get("headimgurl").toString());
                    userInfo.setCountry(userMap.get("country").toString());
                    userInfo.setProvince(userMap.get("province").toString());
                    userInfo.setCity(userMap.get("city").toString());
                    return userInfo;
                }else {
                    throw new WxApiException("weixin [GET_USER_INFO] api response error: " +
                            "status_code = [200], " +
                            "error_message = [" + userMap.get("errmsg").toString() + "]");
                }
            }else {
                throw new WxApiException("weixin [GET_OAUTH_TOKEN] api response error: " +
                        "status_code = [200], " +
                        "error_message = [" + tokenMap.get("errmsg").toString() + "]");
            }
        }catch (MagicHttpException e) {
            throw new WxApiException("Failed to get user info" + e.getMessage());
        }

    }


}
