/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.core;

import com.payway.admin.core.event.SideBarMenuItemClickBusEvent;
import com.payway.admin.core.event.UserSignOutBusEvent;
import com.payway.admin.core.service.event.AdminEventBusService;
import com.payway.admin.ui.component.SideBarMenu;
import com.payway.admin.ui.navigation.AdminNavigator;
import com.payway.admin.ui.view.core.AdminView;
import com.payway.admin.ui.view.sample.DashBoardSampleView;
import com.payway.admin.ui.view.sample.ErrorSampleView;
import com.payway.admin.ui.view.sample.SalesSampleView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * MainView of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@NoArgsConstructor
public final class MainView extends CustomComponentView implements SideBarMenu.SideBarMenuItemButton.SideBarMenuItemButtonClickListener {

    @UiField
    private MenuBar userMenu;

    @UiField
    private SideBarMenu sideBarMenu;

    @UiField
    private CssLayout panelContent;

    public MainView(AdminEventBusService adminEventBusService) {
        super(adminEventBusService);

        setSizeFull();
        setCompositionRoot(Clara.create("MainView.xml", this));

        initializeNavigator();
        initializeUserMenu();
        initializeSideBarMenu();
    }

    /**
     * Ovveride default UI navigator
     */
    private void initializeNavigator() {

        List<AdminView> views = new ArrayList<>();
        views.add(new DashBoardSampleView());
        views.add(new SalesSampleView());
        views.add(new ErrorSampleView());

        //ovveride default ui navigator
        new AdminNavigator(UI.getCurrent(), panelContent, views, "error");
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
        if (getAdminEventBusService() != null) {
            getAdminEventBusService().post(new SideBarMenuItemClickBusEvent(button.getTag()));
        }
    }
}
