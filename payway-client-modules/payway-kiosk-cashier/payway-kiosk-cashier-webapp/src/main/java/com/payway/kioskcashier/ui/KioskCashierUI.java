/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.commons.webapp.core.CommonConstants;
import com.payway.commons.webapp.service.app.user.UserAppService;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.events.LoginExceptionSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginFailSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.payway.kioskcashier.ui.view.core.workspace.TerminalEncashmentWorkspaceView;
import com.payway.messaging.model.user.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
public class KioskCashierUI extends AbstractUI {

    private static final long serialVersionUID = 6737474046471800078L;

    @Autowired
    protected AbstractMainView mainView;

    @Autowired
    @Qualifier(value = "webApps.UserAppService")
    private UserAppService userAppService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        subscribeSessionEventBus();
        registerDetach();
        updateContent();
    }

    private void updateSideBar() {

        mainView.clearWorkspaceView();
        mainView.getSideBarMenu().clearMenuItems();
        mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
        mainView.getSideBarMenu().select(0);
    }

    private void updateContent() {

        UserDto user = userAppService.getUser();
        if (user != null) {

            updateSideBar();
            mainView.getMenuBar().setVisible(false);
            mainView.initializeUserMenu(user.getUsername(), new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());

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
        } else {
            loginView.setTitle("Payway Kiosk Cashier Desktop");
            loginView.initialize();
            setContent(loginView);
        }
    }

    @Override
    protected List<SideBarMenu.MenuItem> getSideBarMenuItems() {
        return Collections.singletonList(new SideBarMenu.MenuItem(TerminalEncashmentWorkspaceView.TERMINAL_ENCASHMENT_WORKSPACE_VIEW_ID, TerminalEncashmentWorkspaceView.TERMINAL_ENCASHMENT_WORKSPACE_VIEW_ID, "Terminal Encashment", new ThemeResource("images/sidebar_terminal_encashment.png"), null, null));
    }

    @Subscribe
    public void processSessionBusEvent(LoginFailSessionBusEvent event) {

        log.error("Bad user sign in (bad auth)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void processSessionBusEvent(LoginExceptionSessionBusEvent event) {

        log.error("Bad user sign in (exception)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void processSessionBusEvent(LoginSuccessSessionBusEvent event) {

        try {

            UserDto userDto = event.getUser();
            if (userDto == null) {
                throw new Exception("User sign in (empty dto)");
            }

            //set params to session
            userAppService.setUser(userDto);
            userAppService.setSessionId(event.getSessionId());

            if (loginView.isRememberMe()) {
                //#hack cookie
                Cookie cookie = new Cookie(CommonAttributes.REMEMBER_ME.value(), "");
                cookie.setMaxAge(CommonConstants.REMEMBER_ME_COOKIE_MAX_AGE);
                UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
            } else {
                //#hack cookie
                Cookie cookie = new Cookie(CommonAttributes.REMEMBER_ME.value(), "");
                cookie.setMaxAge(0);
                UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
            }

            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            updateContent();

        } catch (Exception ex) {
            log.error("Bad user sign in - {}", ex);
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
        }
    }
}
