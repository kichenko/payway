/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.core;

import com.payway.advertising.core.event.UserSignOutBusEvent;
import com.payway.advertising.core.service.event.AdminEventBusService;
import com.payway.advertising.ui.component.SideBarMenu;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * MainView of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@SpringView(name = "main")
public final class MainView extends CustomComponentView implements SideBarMenu.SideBarMenuItemButton.SideBarMenuItemButtonClickListener, View {

    @Autowired
    public SpringViewProvider viewProvider;

    @UiField
    private MenuBar userMenu;

    @UiField
    private SideBarMenu sideBarMenu;

    @UiField
    private CssLayout panelContent;

    public MainView() {
        setSizeFull();
        setCompositionRoot(Clara.create("MainView.xml", this));
        initializeUserMenu();
        initializeSideBarMenu();
    }

    @PostConstruct
    void init() {
        Navigator navigator = new Navigator(UI.getCurrent(), panelContent);
        navigator.addProvider(viewProvider);
    }

    /**
     * Build user menu items
     */
    private void initializeUserMenu() {

        MenuBar.MenuItem settingsItem = userMenu.addItem("kichenko", null);

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
                AdminEventBusService adminEventBusService = MainView.this.getAdminEventBusService();
                if (adminEventBusService != null) {
                    adminEventBusService.post(new UserSignOutBusEvent());
                }
            }
        });
    }

    /**
     * Build user side bar menu items
     */
    private void initializeSideBarMenu() {
        sideBarMenu.addMenuItem("dashboard", "DashBoard", FontAwesome.HOME, this);
        sideBarMenu.addMenuItem("sales", "Sales", FontAwesome.BAR_CHART_O, this);
        sideBarMenu.addMenuItem("transactions", "Transactions", FontAwesome.TABLE, this);
        sideBarMenu.addMenuItem("reports", "Reports", FontAwesome.FILE_TEXT_O, this);
        sideBarMenu.addMenuItem("schedule", "Schedule", FontAwesome.CALENDAR_O, this);
    }

    /**
     * SideBar menu item click listener
     *
     * @param button Clicked menu item button
     * @param event Clicked menu item button event
     */
    @Override
    public void clickSideBarMenuItemButton(SideBarMenu.SideBarMenuItemButton button, Button.ClickEvent event) {
        UI.getCurrent().getNavigator().navigateTo(button.getTag());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //
    }
}
