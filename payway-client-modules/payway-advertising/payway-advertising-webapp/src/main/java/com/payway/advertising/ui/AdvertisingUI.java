/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.core.service.UserService;
import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.service.app.user.UserAppService;
import com.payway.advertising.core.service.config.apply.ApplyConfigurationStatus;
import com.payway.advertising.core.service.config.apply.ApplyStatus;
import com.payway.advertising.core.service.config.apply.ConfigurationApplyService;
import com.payway.advertising.model.DbUser;
import com.payway.advertising.ui.bus.events.CloseNotificationsButtonPopupWindowsEvent;
import com.payway.advertising.ui.component.notification.NotificationsButtonPopupWindow;
import com.payway.advertising.ui.component.notification.events.ApplyConfigurationNotificationEvent;
import com.payway.advertising.ui.view.core.AdvertisingMainView;
import com.payway.advertising.ui.view.core.AdvertisingSettingsWindow;
import com.payway.advertising.ui.view.workspace.content.AdvertisingContentConfigurationView;
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
import com.payway.messaging.model.user.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * AdvertisingUI
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Slf4j
@SpringUI
@Theme("default")
@PreserveOnRefresh
@Widgetset("com.payway.advertising.AdvertisingWidgetSet")
public class AdvertisingUI extends AbstractUI {

    private static final long serialVersionUID = -2447415985839871519L;

    @Autowired
    private AdvertisingMainView mainView;

    @Autowired
    private LoginView loginView;

    @Autowired
    @Qualifier(value = "userAppService")
    private UserAppService userAppService;

    @Autowired
    @Qualifier(value = "userService")
    private UserService userService;

    @Autowired
    @Qualifier(value = "settingsAppService")
    SettingsAppService settingsAppService;

    @Getter(onMethod = @_({
        @Override}))
    @Autowired
    @Qualifier(value = "sessionEventBus")
    private SessionEventBus sessionEventBus;

    @Getter
    @Autowired
    @Qualifier(value = "configurationApplyService")
    private ConfigurationApplyService configurationApplyService;

    @Getter
    @Autowired
    @Qualifier(value = "agentFileOwnerService")
    private AgentFileOwnerService agentFileOwnerService;

    @Override
    protected void init(VaadinRequest request) {

        settingsAppService.setContextPath(request.getContextPath());
        updateContent();

        addDetachListener(new DetachListener() {
            private static final long serialVersionUID = -327258454602850406L;

            @Override
            public void detach(DetachEvent event) {
                cleanUp();
            }
        });

        sessionEventBus.addSubscriber(this);
    }

    private void cleanUp() {
        sessionEventBus.removeSubscriber(this);
        sessionEventBus.removeSubscriber(mainView.getBtnNotifications());
    }

    private void closeNotificationsButtonPopupWindow() {
        for (Window window : getUI().getWindows()) {
            if (window instanceof NotificationsButtonPopupWindow) {
                window.close();
                break;
            }
        }
    }

    private void sendApplyConfigurationStatusNotification(ApplyConfigurationStatus status) {
        sessionEventBus.sendNotification(new ApplyConfigurationNotificationEvent(status.getLogin(), status.getStartTime(), status.getStatus(), status.getStatusTime(), status.getArgs()));
    }

    @Override
    protected List<SideBarMenu.MenuItem> getSideBarMenuItems() {
        List<SideBarMenu.MenuItem> items = new ArrayList<>(1);
        items.add(new SideBarMenu.MenuItem("configuration", AdvertisingContentConfigurationView.ADVERTISING_CONTENT_WORKSPACE_VIEW_ID, "Configuration", new ThemeResource("images/sidebar_configuration.png"), null, null));
        return items;
    }

    private void refreshApplyConfigNotification() {
        ApplyConfigurationStatus status = configurationApplyService.getStatus();

        if (status != null && !ApplyStatus.None.equals(status.getStatus())) {
            sessionEventBus.sendNotification(new ApplyConfigurationNotificationEvent(status.getLogin(), status.getStartTime(), status.getStatus(), status.getStatusTime(), status.getArgs()));
        }
    }

    @Override
    protected List<AbstractMainView.UserMenuItem> getMenuBarItems() {

        List<AbstractMainView.UserMenuItem> menus = new ArrayList<>(2);

        menus.add(new AbstractMainView.UserMenuItem("Settings", new ThemeResource("images/user_menu_item_settings.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                new AdvertisingSettingsWindow("Settings", settingsAppService).show();
            }
        }, true));

        menus.add(super.getMenuBarItems().get(0));

        return menus;
    }

    /**
     * Refresh notifications on user sig in.
     *
     * Ex: then user signin and configuration is applying at this time - it's
     * need to notify user about this action.
     */
    private void refreshNotifications() {
        refreshApplyConfigNotification();
    }

    private void updateContent() {

        DbUser user = userAppService.getUser();
        if (user != null) {
            mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
            mainView.initializeUserMenu(user.getLogin(), new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());
            sessionEventBus.addSubscriber(mainView.getBtnNotifications());
            mainView.getSideBarMenu().select(0);

            refreshNotifications();
            setContent(mainView);
        } else {
            loginView.setTitle("Payway Advertising Desktop");
            loginView.initialize();
            setContent(loginView);
        }
    }

    @Subscribe
    public void processSessionBusEvent(ApplyConfigurationStatus event) {
        sendApplyConfigurationStatusNotification((ApplyConfigurationStatus) event);
    }

    @Subscribe
    public void processSessionBusEvent(CloseNotificationsButtonPopupWindowsEvent event) {
        closeNotificationsButtonPopupWindow();
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

            DbUser user = userService.findUserByLogin(userDto.getUsername(), true);
            if (user == null) {
                throw new Exception("Bad user sign in (user not found)");
            }

            user.setToken(event.getSessionId());

            //set params to session
            userAppService.setUser(user);

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
            log.error("Bad user sign in", ex);
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
        }
    }
}
