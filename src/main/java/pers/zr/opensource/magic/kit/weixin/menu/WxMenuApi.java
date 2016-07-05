package pers.zr.opensource.magic.kit.weixin.menu;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zr.opensource.magic.kit.http.*;
import pers.zr.opensource.magic.kit.http.client.MagicTrustedHttpsClient;
import pers.zr.opensource.magic.kit.weixin.WxApiException;
import pers.zr.opensource.magic.kit.weixin.WxResponseValidator;
import pers.zr.opensource.magic.kit.weixin.common.WxCommonApi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义菜单API
 * 1、自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
 * 2、一级菜单最多4个汉字，二级菜单最多7个汉字，多出来的部分将会以“...”代替。
 * 3、创建自定义菜单后，菜单的刷新策略是，在用户进入公众号会话页或公众号profile页时，
 *    如果发现上一次拉取菜单的请求在5分钟以前，就会拉取一下菜单，如果菜单有更新，就会刷新客户端的菜单。
 *    测试时可以尝试取消关注公众账号后再次关注，则可以看到创建后的效果。
 */
public class WxMenuApi {

    //自定义菜单创建接口
    private static final String MENU_CREATE_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=[access_token]";

    //自定义菜单查询接口
    private static final String MENU_QUERY_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=[access_token]";

    //自定义菜单删除接口
    private static final String MENU_DELETE_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=[access_token]";

    private static final Logger log = LoggerFactory.getLogger(WxMenuApi.class);
    
    /**
     * 删除指定公众号自定义菜单
     * @param commonAccessToken 通用AccessToken
     * @throws WxApiException
     */
    public static void deleteMenus(String commonAccessToken) throws WxApiException {
       try{
           String url = MENU_DELETE_URL_TEMPLATE.replace("[access_token]", commonAccessToken);
           MagicHttpRequest request = new MagicHttpRequest(url, RequestMethodType.GET);
           MagicHttpResponse response = MagicTrustedHttpsClient.getInstance().send(request);
           if(log.isDebugEnabled()) {
               log.debug("delete menus response: " + response.getBodyAsString());

           }
           if(response.getStatusCode() != 200) {
               throw new WxApiException("weixin [DELETE_MENUS] api response error: " +
                       "status_code = [" + response.getStatusCode() + "], " +
                       "error_message = [" + response.getBodyAsString() + "]");
           }
           Map responseMap = JSON.parseObject(response.getBodyAsString(), Map.class);
           if(WxResponseValidator.isErrorOccured(responseMap)) {
               throw new WxApiException("weixin [DELETE_MENUS] api response error: " +
                       "status_code = [200], " +
                       "error_message = [" + responseMap.get("errmsg").toString() + "]");
           }
       }catch (MagicHttpException e) {
           throw new WxApiException("Failed to delete menus, caused by" + e.getMessage());
       }
    }

    /**
     * 创建指定公众号自定义菜单
     * @param commonAccessToken 通用AccessToken
     * @param buttonList 菜单列表
     * @throws WxApiException
     */
    public static void createMenus(String commonAccessToken, List<MenuButton> buttonList) throws WxApiException {
        try{

            Map<String, List<MenuButton>> buttonsMap = new HashMap<>();
            buttonsMap.put("button", buttonList);
            String buttonsJson = JSON.toJSONString(buttonsMap);
            String url = MENU_CREATE_URL_TEMPLATE.replace("[access_token]", commonAccessToken);
            MagicHttpRequest request = new MagicHttpRequest(url, RequestMethodType.POST, RequestContentType.APPLICATION_JSON);
            request.setParams(buttonsMap);
            MagicHttpResponse response = MagicTrustedHttpsClient.getInstance().send(request);
            if(log.isDebugEnabled()) {
                log.debug("Button Definition Data: " + buttonsJson);
                log.debug("Create menus response: " + response.getBodyAsString());

            }
            if(response.getStatusCode() != 200) {
                throw new WxApiException("weixin [CREATE_MENUS] api response error: " +
                        "status_code = [" + response.getStatusCode() + "]," +
                        "error_message = [" + response.getBodyAsString() + "]");
            }
            Map responseMap = JSON.parseObject(response.getBodyAsString(), Map.class);
            if(WxResponseValidator.isErrorOccured(responseMap)) {
                throw new WxApiException("weixin [CREATE_MENUS] api response error: " +
                        "status_code = [200], " +
                        "error_message = [" + responseMap.get("errmsg").toString() + "]");
            }
        }catch (MagicHttpException e) {
            throw new WxApiException("Failed to create menus, caused by " + e.getMessage());
        }
    }

    public static String getMenus(String commonAccessToken) throws WxApiException {
        try{
            String url = MENU_QUERY_URL_TEMPLATE.replace("[access_token]", commonAccessToken);
            MagicHttpRequest request = new MagicHttpRequest(url, RequestMethodType.GET);
            MagicHttpResponse response = MagicTrustedHttpsClient.getInstance().send(request);
            if(log.isDebugEnabled()) {
                log.debug("get menus response: " + response.getBodyAsString());
            }
            if(response.getStatusCode() != 200) {
                throw new WxApiException("weixin [GET_MENUS] api response error: " +
                        "status_code = [" + response.getStatusCode() + "], " +
                        "error_message = [" + response.getBodyAsString() + "]");
            }
            Map responseMap = JSON.parseObject(response.getBodyAsString(), Map.class);
            if(WxResponseValidator.isErrorOccured(responseMap)) {
                throw new WxApiException("weixin [GET_MENUS] api response error: " +
                        "status_code = [200], " +
                        "error_message = [" + responseMap.get("errmsg").toString() + "]");
            }
            return response.getBodyAsString();
        }catch (MagicHttpException e) {
            throw new WxApiException("Failed to get menus, caused by " + e.getMessage());
        }


    }
    public static void main(String[] args) throws Exception{
        String scanButtonName = new String("扫码有奖".getBytes("UTF-8"));;
        String mallButtonName = new String("恒大商城".getBytes("UTF-8"));;
        MenuButton scanButton = MenuButtonFactory.getInstance().createKeyMenuButton(MenuButtonType.SCANCODE_PUSH, scanButtonName, "KEY_SCAN_QRCODE");
        MenuButton mallButton = MenuButtonFactory.getInstance().createUrlMenuButton(MenuButtonType.VIEW, mallButtonName, "http://m.xiwanglife.com/bq/mall");
        String downloadButtionName = new String("APP下载".getBytes("UTF-8"));
        MenuButton downloadButtion = MenuButtonFactory.getInstance().createUrlMenuButton(MenuButtonType.VIEW, downloadButtionName, "http://www.xiwanglife.com/");

        List<MenuButton> buttonList = new ArrayList<>();
        buttonList.add(scanButton);
        buttonList.add(mallButton);
        buttonList.add(downloadButtion);

        String accessToken = WxCommonApi.getCommonAccessToken("xxx", "xxxxx");
        System.out.println(accessToken);
//        WxMenuApi.getMenus(accessToken);
        WxMenuApi.createMenus(accessToken, buttonList);
//        WxMenuApi.deleteMenus(accessToken);


    }




}
