package pers.zr.opensource.magic.kit.weixin.menu;

/**
 * 自定义菜单类型
 */
public enum MenuButtonType {

    CLICK("click"),   //点击推事件

    VIEW("view"),  //跳转URL

    SCANCODE_PUSH("scancode_push"), //扫码推事件

    SCANCODE_WAITMSG("scancode_waitmsg"),  //扫码推事件且弹出“消息接收中”提示框

    PIC_SYSPHOTO("pic_sysphoto"),  //弹出系统拍照发图

    PIC_PHOTO_OR_ALBUM("pic_photo_or_album"), //弹出拍照或者相册发图

    PIC_WEIXIN("pic_weixin"),  //弹出微信相册发图器

    LOCATION_SELECT("location_select"), //弹出地理位置选择器

    MEDIA_ID("media_id"),  //下发消息（除文本消息）

    VIEW_LIMITED("view_limited"); //跳转图文消息URL

    public String value;

    private MenuButtonType(String value) {
        this.value = value;
    }

}
