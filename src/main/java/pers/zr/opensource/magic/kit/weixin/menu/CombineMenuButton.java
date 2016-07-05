package pers.zr.opensource.magic.kit.weixin.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合菜单，用于组合二级菜单
 */
public class CombineMenuButton extends MenuButton {

    private List<MenuButton> sub_button = new ArrayList<>();

    public List<MenuButton> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<MenuButton> sub_button) {
        this.sub_button = sub_button;
    }
}
