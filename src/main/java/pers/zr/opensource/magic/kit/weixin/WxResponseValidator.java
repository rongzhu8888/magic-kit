package pers.zr.opensource.magic.kit.weixin;

import java.util.Map;

public class WxResponseValidator {

    public static boolean isErrorOccured(Map map) {
        return (map!=null && map.get("errcode")!=null && !map.get("errcode").toString().equals("0"));

    }


}
