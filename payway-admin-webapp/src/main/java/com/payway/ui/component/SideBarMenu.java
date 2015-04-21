/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.ui.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;

/**
 * SideBarMenu of admin webapp
 *
 * @author Sergey Kichenko
 * @created 21.04.15 00:00
 */
public final class SideBarMenu extends CustomComponent {

    private final CssLayout menuContent = new CssLayout();
    private SideBarMenuItemButton selectedButton;

    public static class SideBarMenuItemButton extends Button {

        public interface SideBarMenuItemButtonClickListener {

            void clickButton(final SideBarMenuItemButton button, final Button.ClickEvent event);
        }

        public SideBarMenuItemButton(String caption, Resource icon, final SideBarMenuItemButtonClickListener listener) {
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
                        listener.clickButton(SideBarMenuItemButton.this, event);
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

        //@@
        buildMenu();
    }

    //@@
    private void buildMenu() {
        SideBarMenuItemButton.SideBarMenuItemButtonClickListener l = new SideBarMenuItemButton.SideBarMenuItemButtonClickListener() {
            @Override
            public void clickButton(SideBarMenuItemButton button, Button.ClickEvent event) {
                Notification.show("Notification", "Not implemented", Notification.Type.WARNING_MESSAGE);
            }
        };

        menuContent.addComponent(new SideBarMenuItemButton("DashBoard", FontAwesome.HOME, l));
        menuContent.addComponent(new SideBarMenuItemButton("Sales", FontAwesome.BAR_CHART_O, l));
        menuContent.addComponent(new SideBarMenuItemButton("Transactions", FontAwesome.TABLE, l));
        menuContent.addComponent(new SideBarMenuItemButton("Reports", FontAwesome.FILE_TEXT_O, l));
        menuContent.addComponent(new SideBarMenuItemButton("Schedule", FontAwesome.CALENDAR_O, l));
    }

    public void addMenuItem(String caption, Resource icon, SideBarMenuItemButton.SideBarMenuItemButtonClickListener listener) {
        menuContent.addComponent(new SideBarMenuItemButton(caption, icon, listener));
    }

    public void clearMenuItems() {
        menuContent.removeAllComponents();
    }
}
