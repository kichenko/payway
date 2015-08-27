/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.service.app.user.WebAppUser;
import com.payway.commons.webapp.ui.AbstractLoginUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.payway.kioskcashier.ui.view.core.workspace.BankCashDepositWorkspaceView;
import com.payway.kioskcashier.ui.view.core.workspace.QuickEncashmentCheckWorkspaceView;
import com.payway.kioskcashier.ui.view.core.workspace.TerminalEncashmentWorkspaceView;
import com.payway.messaging.model.user.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * KioskCashierUI
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
@Slf4j
@SpringUI
@Theme("default")
@PreserveOnRefresh
@Widgetset("com.payway.kioskcashier.KioskCashierWidgetSet")
public class KioskCashierUI extends AbstractLoginUI {

    private static final long serialVersionUID = 6737474046471800078L;

    @Autowired
    protected AbstractMainView mainView;

    private void updateSideBar() {

        mainView.clearWorkspaceView();
        mainView.getSideBarMenu().clearMenuItems();
        mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
        mainView.getSideBarMenu().select(0);
    }

    @Override
    protected String getLoginTitle() {
        return "Payway Kiosk Cashier Dashboard";
    }

    @Override
    protected void setupWorkspaceContent() {

        updateSideBar();
        mainView.getMenuBar().setVisible(false);
        mainView.initializeUserMenu(webAppUserService.getUser().getLogin(), new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());

        //subscribe workspace on session events
        mainView.setViewActivateStateChangeListener(new AbstractMainView.ViewActivateStateChangeListener() {

            @Override
            public void onActivate(WorkspaceView workspaceView) {
                if (workspaceView != null) {
                    getSessionEventBus().addSubscriber(workspaceView);
                }
            }

            @Override
            public void onDeactivate(WorkspaceView workspaceView) {
                if (workspaceView != null) {
                    getSessionEventBus().removeSubscriber(workspaceView);
                }
            }
        });

        setContent(mainView);
    }

    @Override
    protected List<SideBarMenu.MenuItem> getSideBarMenuItems() {

        List<SideBarMenu.MenuItem> menus = new ArrayList<>();

        menus.add(new SideBarMenu.MenuItem(TerminalEncashmentWorkspaceView.TERMINAL_ENCASHMENT_WORKSPACE_VIEW_ID, TerminalEncashmentWorkspaceView.TERMINAL_ENCASHMENT_WORKSPACE_VIEW_ID, "Terminal Encashment", new ThemeResource("images/sidebar_terminal_encashment.png"), null, null));
        menus.add(new SideBarMenu.MenuItem(BankCashDepositWorkspaceView.BANK_CASH_DEPOSIT_WORKSPACE_VIEW_ID, BankCashDepositWorkspaceView.BANK_CASH_DEPOSIT_WORKSPACE_VIEW_ID, "Cash deposit", new ThemeResource("images/sidebar_bank_cash_deposit.png"), null, null));
        menus.add(new SideBarMenu.MenuItem(QuickEncashmentCheckWorkspaceView.QUICK_ENCASHMENT_WORKSPACE_VIEW_ID, QuickEncashmentCheckWorkspaceView.QUICK_ENCASHMENT_WORKSPACE_VIEW_ID, "Quick encashment check", new ThemeResource("images/sidebar_quick_encashment_check.png"), null, null));
        return menus;
    }

    @Subscribe
    public void processSessionBusEvent(LoginSuccessSessionBusEvent event) {

        try {

            UserDto userDto = event.getUser();
            if (userDto == null) {
                throw new Exception("User sign in (empty dto)");
            }

            webAppUserService.setUser(new WebAppUser(userDto.getUsername(), "", event.getSessionId()));
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            setupWorkspaceContent();
        } catch (Exception ex) {
            log.error("Bad user sign in - {}", ex);
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
        }
    }
}
