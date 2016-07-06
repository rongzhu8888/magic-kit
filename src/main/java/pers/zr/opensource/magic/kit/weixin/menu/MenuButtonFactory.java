package pers.zr.opensource.magic.kit.weixin.menu;

import pers.zr.opensource.magic.kit.weixin.WxApiException;

import java.util.List;

/**
 * 菜单按钮工厂类
 */
public class MenuButtonFactory {

    private static MenuButtonFactory menuButtonFactory = new MenuButtonFactory();

    private MenuButtonFactory() {}

    public static MenuButtonFactory getInstance() {
        return menuButtonFactory;
    }

    public MenuButton createKeyMenuButton(MenuButtonType type, String name, String key) throws WxApiException {
        if(MenuButtonType.CLICK == type || MenuButtonType.SCANCODE_PUSH == type ||
                MenuButtonType.SCANCODE_WAITMSG == type || MenuButtonType.LOCATION_SELECT == type ||
                MenuButtonType.PIC_PHOTO_OR_ALBUM == type || MenuButtonType.PIC_SYSPHOTO == type ||
                MenuButtonType.PIC_WEIXIN == type) {
            KeyMenuButton menuButton = new KeyMenuButton();
            menuButton.setName(name);
            menuButton.setType(type.value);
            menuButton.setKey(key);
            return menuButton;
        }else {
            throw new WxApiException("menu button type error");
        }
    }

    public MenuButton createUrlMenuButton(MenuButtonType type, String name, String url) throws WxApiException {
        if(MenuButtonType.VIEW == type) {
            UrlMenuButton menuButton = new UrlMenuButton();
            menuButton.setType(type.value);
            menuButton.setName(name);
            menuButton.setUrl(url);
            return menuButton;
        }else {
            throw new WxApiException("menu button type error");
        }
    }

    public MenuButton createMediaMenuButton(MenuButtonType type, String name, String mediaId) throws WxApiException {
        if(MenuButtonType.MEDIA_ID == type || MenuButtonType.VIEW_LIMITED == type) {
            MediaMenuButton menuButton = new MediaMenuButton();
            menuButton.setName(name);
            menuButton.setType(type.value);
            menuButton.setMedia_id(mediaId);
            return menuButton;
        }else {
            throw new WxApiException("menu button type error");
        }
    }

    public MenuButton createCombineMenuButton(String name, List<MenuButton> subMenuButtonList) throws WxApiException {
        CombineMenuButton menuButton = new CombineMenuButton();
        menuButton.setName(name);
        if(subMenuButtonList != null) {
            menuButton.getSub_button().addAll(subMenuButtonList);
        }
        return menuButton;
    }
}
