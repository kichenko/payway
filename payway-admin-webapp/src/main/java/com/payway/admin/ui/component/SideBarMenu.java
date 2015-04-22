/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import lombok.Getter;
import lombok.Setter;

/**
 * SideBarMenu
 *
 * @author Sergey Kichenko
 * @created 21.04.15 00:00
 */
public final class SideBarMenu extends CustomComponent {

    private final CssLayout menuContent = new CssLayout();
    private SideBarMenuItemButton selectedButton;

    public static class SideBarMenuItemButton extends Button {

        @Getter
        @Setter
        private String tag;

        public interface SideBarMenuItemButtonClickListener {

            void clickSideBarMenuItemButton(final SideBarMenuItemButton button, final Button.ClickEvent event);
        }

        public SideBarMenuItemButton(final String tag, final String caption, final Resource icon, final SideBarMenuItemButtonClickListener listener) {
            setTag(tag);
            setIcon(icon);
            setCaption(caption);
            setPrimaryStyleName("sidebar-menu-item");

            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {

                    SideBarMenuItemButton btn = ((SideBarMenu) SideBarMenuItemButton.this.getParent().getParent()).selectedButton;
                    if (btn != null) {
                        btn.removeStyleName("selected");
                    }

                    if (listener != null) {
                        listener.clickSideBarMenuItemButton(SideBarMenuItemButton.this, event);
                    }

                    SideBarMenuItemButton.this.addStyleName("selected");
                    ((SideBarMenu) SideBarMenuItemButton.this.getParent().getParent()).selectedButton = SideBarMenuItemButton.this;
                }
            });
        }
    }

    public SideBarMenu() {
        setSizeFull();
        addStyleName("sidebar");
        setCompositionRoot(menuContent);
    }

    public void addMenuItem(final String tag, final String caption, final Resource icon, final SideBarMenuItemButton.SideBarMenuItemButtonClickListener listener) {
        menuContent.addComponent(new SideBarMenuItemButton(tag, caption, icon, listener));
    }

    public void clearMenuItems() {
        menuContent.removeAllComponents();
    }
}
