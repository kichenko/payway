/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.ui.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;

/**
 * SideBarMenu of admin webapp
 *
 * @author Sergey Kichenko
 * @created 21.04.15 00:00
 */
public final class SideBarMenu extends CustomComponent {

    public static class SideBarMenuItemButton extends Button {

        public SideBarMenuItemButton(String caption, Resource icon) {
            setPrimaryStyleName("sidebar-menu-valo-menu-item");
            setCaption(caption);
            setIcon(icon);
        }

        public SideBarMenuItemButton(String caption) {
            setPrimaryStyleName("sidebar-menu-valo-menu-item");
            setCaption(caption);
        }

        public SideBarMenuItemButton() {
            setPrimaryStyleName("sidebar-menu-valo-menu-item");
        }
    }

    public SideBarMenu() {
        addStyleName("sidebar");
        setSizeFull();
        setCompositionRoot(buildContent());
    }

    private Component buildContent() {

        CssLayout menuContent = new CssLayout();

        menuContent.addStyleName("sidebar-menu"); //?
        //menuContent.addStyleName("sidebar-menu-valo-menu-part");
        //menuContent.addStyleName("sidebar-menu-no-vertical-drag-hints");
        //menuContent.addStyleName("sidebar-menu-no-horizontal-drag-hints");
        //menuContent.setHeight(100.0f, Unit.PERCENTAGE);

        menuContent.addComponent(buildMenu());

        return menuContent;
    }

    private Component buildMenu() {
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("sidebar-menu-valo-menuitems");
        menuItemsLayout.setHeight(100.0f, Unit.PERCENTAGE);
        menuItemsLayout.addComponent(new SideBarMenuItemButton("Hello"));

        return menuItemsLayout;
    }
}
