package pers.zr.opensource.magic.kit.weixin.menu;

/**
 * URL跳转类BUTTON，可与网页授权结合获取用户信息,包含以下类型：
 * view
 */
public class UrlMenuButton extends MenuButton {

    private String url;

    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
