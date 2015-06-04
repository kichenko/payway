/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SideBarMenu
 *
 * @author Sergey Kichenko
 * @created 21.04.15 00:00
 */
public final class SideBarMenu extends CustomComponent {

    private static final long serialVersionUID = 7791705742904164938L;

    private final CssLayout menuContent = new CssLayout();
    private SideBarMenuButton selectedButton;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuItem {

        private String tag;
        private String caption;
        private Resource icon;
    }

    @NoArgsConstructor
    public static class SideBarMenuButton extends Button {

        private static final long serialVersionUID = -1739353046736625232L;

        @Getter
        @Setter
        private String tag;

        public interface SideBarMenuButtonClickListener {

            void onClickSideBarMenuItemButton(final SideBarMenuButton button, final Button.ClickEvent event);
        }

        public SideBarMenuButton(final String tag, final String caption, final Resource icon, final SideBarMenuButtonClickListener listener) {
            setTag(tag);
            setIcon(icon);
            setCaption(caption);
            setPrimaryStyleName("sidebar-menu-item");

            addClickListener(new ClickListener() {
                private static final long serialVersionUID = 5019806363620874205L;

                @Override
                public void buttonClick(final ClickEvent event) {

                    SideBarMenuButton btn = ((SideBarMenu) SideBarMenuButton.this.getParent().getParent()).selectedButton;
                    if (btn != null) {
                        btn.removeStyleName("selected");
                    }

                    if (listener != null) {
                        listener.onClickSideBarMenuItemButton(SideBarMenuButton.this, event);
                    }

                    SideBarMenuButton.this.addStyleName("selected");
                    ((SideBarMenu) SideBarMenuButton.this.getParent().getParent()).selectedButton = SideBarMenuButton.this;
                }
            });
        }
    }

    public SideBarMenu() {
        setSizeFull();
        addStyleName("sidebar");
        setCompositionRoot(menuContent);
    }

    public void addMenuItem(final SideBarMenu.MenuItem item, final SideBarMenuButton.SideBarMenuButtonClickListener listener) {
        menuContent.addComponent(new SideBarMenuButton(item.getTag(), item.getCaption(), item.icon, listener));
    }

    public void clearMenuItems() {
        menuContent.removeAllComponents();
    }

    public boolean select(int index) {
        if (index >= 0 && index < menuContent.getComponentCount()) {
            SideBarMenuButton btn = (SideBarMenuButton) menuContent.getComponent(index);
            if (btn != null) {
                btn.click();
                return true;
            }
        }

        return false;
    }
}
