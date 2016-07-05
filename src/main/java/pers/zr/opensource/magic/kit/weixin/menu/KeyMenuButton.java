package pers.zr.opensource.magic.kit.weixin.menu;

/**
 * 消息接口推送Button，包含以下类型：
 * click
 * scancode_push
 * scancode_waitmsg
 * pic_sysphoto
 * pic_photo_or_album
 * pic_weixin
 * location_select
 */
public class KeyMenuButton extends MenuButton {

    private String type;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
