/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.commons.webapp.core.CommonConstants;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.bus.events.LoginExceptionSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginFailSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.LoginView;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.payway.kioskcashier.service.app.user.UserAppService;
import com.payway.messaging.model.user.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.Getter;
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

    @Getter(onMethod = @_({
        @Override}))
    @Autowired
    @Qualifier(value = "sessionEventBus")
    private SessionEventBus sessionEventBus;

    @Autowired
    private AbstractMainView mainView;

    @Autowired
    private LoginView loginView;

    @Autowired
    private UserAppService userAppService;

    @Override
    protected List<SideBarMenu.MenuItem> getSideBarMenuItems() {
        return new ArrayList<>(0);
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        addDetachListener(new DetachListener() {
            private static final long serialVersionUID = -327258454602850406L;

            @Override
            public void detach(DetachEvent event) {
                cleanUp();
            }
        });

        updateContent();
        sessionEventBus.addSubscriber(this);
    }

    private void cleanUp() {
        sessionEventBus.removeSubscriber(this);
    }

    private void updateContent() {

        UserDto user = userAppService.getUser();
        if (user != null) {

            //updateSideBar();
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

            //DbUser user = userService.findUserByLogin(userDto.getUsername(), true);
            //if (user == null) {
            //    throw new Exception("Bad user sign in (user not found)");
            //}
            //user.setToken(event.getSessionId());
            //set params to session
            //userAppService.setUser(user);
            if (loginView.isRememberMe()) {
                Cookie cookie = new Cookie(CommonAttributes.REMEMBER_ME.value(), user.getToken());
                cookie.setMaxAge(CommonConstants.REMEMBER_ME_COOKIE_MAX_AGE);
                //#hack cookie
                UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
            } else {
                Cookie cookie = new Cookie(CommonAttributes.REMEMBER_ME.value(), "");
                cookie.setMaxAge(0);
                //#hack cookie
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
