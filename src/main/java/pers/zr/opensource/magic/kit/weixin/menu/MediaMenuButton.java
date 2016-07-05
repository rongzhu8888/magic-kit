package pers.zr.opensource.magic.kit.weixin.menu;

/**
 * 素材下发Button，包含以下类型：
 * media_id
 * view_limited
 *
 */
public class MediaMenuButton extends MenuButton {

    private String media_id;

    private String type;


    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
