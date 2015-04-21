/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.ui.view;

import com.payway.ui.component.SideBarMenu;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * MainView of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
public final class MainView extends CustomComponent {

    @UiField
    private MenuBar userMenu;

    @UiField
    private SideBarMenu sideBarMenu;

    private MenuBar.MenuItem settingsItem;

    public MainView() {
        setSizeFull();
        setCompositionRoot(Clara.create("MainView.xml", this));

        //build ui
        buildUserMenu();
        buildSideBar();
    }

    private void buildUserMenu() {

        settingsItem = userMenu.addItem("kichenko", null);

        settingsItem.addItem("Edit Profile", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                Notification.show("Notification", "Not implemented", Notification.Type.WARNING_MESSAGE);
            }
        });
        settingsItem.addItem("Preferences", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                Notification.show("Notification", "Not implemented", Notification.Type.WARNING_MESSAGE);
            }
        });
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                Notification.show("Notification", "Not implemented", Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void buildSideBar() {
        //sideBarMenu.buildMenu(Collections.singletonList(new SideBarMenu.SideBarMenuItemButton("Hello", null)));
    }
}
