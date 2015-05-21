/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.payway.advertising.core.app.bus.EventBusBridge;
import com.payway.advertising.core.service.ConfigurationService;
import com.payway.advertising.core.service.UserService;
import com.payway.advertising.core.service.app.user.UserAppService;
import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.service.notification.ApplyConfigurationNotificationEvent;
import com.payway.advertising.core.service.notification.NotificationService;
import com.payway.advertising.messaging.ResponseCallBack;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;
import com.payway.advertising.ui.bus.UIEventBus;
import com.payway.advertising.ui.component.SideBarMenu;
import com.payway.advertising.ui.view.core.Attributes;
import com.payway.advertising.ui.view.core.Constants;
import com.payway.advertising.ui.view.core.LoginView;
import com.payway.advertising.ui.view.core.MainView;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.response.auth.AbstractAuthCommandResponse;
import com.payway.messaging.message.response.auth.AuthBadCredentialsCommandResponse;
import com.payway.messaging.message.response.auth.AuthSuccessComandResponse;
import com.payway.messaging.model.message.auth.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Главное UI
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Slf4j
@SpringUI
@Theme("default")
@PreserveOnRefresh
@Widgetset("com.payway.advertising.AdvertisingWidgetSet")
public class AdvertisingUI extends AbstractUI implements ResponseCallBack<SuccessResponse, ExceptionResponse> {

    @Autowired
    private MainView mainView;

    @Autowired
    private LoginView loginView;

    @Autowired
    @Qualifier(value = "userAppService")
    private UserAppService userAppService;

    @Autowired
    @Qualifier(value = "userService")
    private UserService userService;

    @Autowired
    @Qualifier(value = "configurationService")
    private ConfigurationService configurationService;

    @Autowired
    @Qualifier(value = "settingsAppService")
    SettingsAppService settingsAppService;

    @Autowired
    @Qualifier(value = "notificationService")
    private NotificationService notificationService;

    @Autowired
    @Qualifier(value = "eventBusBridge")
    private EventBusBridge eventBusBridge;

    @Getter
    @Autowired
    @Qualifier(value = "uiEventBus")
    private UIEventBus eventBus;

    @Override
    protected void init(VaadinRequest request) {
        settingsAppService.setContextPath(request.getContextPath());
        updateContent();
    }

    private Collection<SideBarMenu.MenuItem> getSideBarMenuItems() {
        Collection<SideBarMenu.MenuItem> items = new ArrayList<>(5);
        items.add(new SideBarMenu.MenuItem("content-configuration", "Configuration", new ThemeResource("images/sidebar_configuration.png")));
        items.add(new SideBarMenu.MenuItem("reports", "Reports", new ThemeResource("images/sidebar_report.png")));
        return items;
    }

    private Collection<ImmutableTriple<String, Resource, MenuBar.Command>> getMenuBarItems() {
        return Collections.singletonList(
                new ImmutableTriple<String, Resource, MenuBar.Command>(
                        "Sign Out", new ThemeResource("images/user_menu_item_logout.png"), new MenuBar.Command() {
                            @Override
                            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                                VaadinSession.getCurrent().close();
                                UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
                                VaadinSession.getCurrent().close();
                                Page.getCurrent().reload();
                            }
                        }));
    }

    /**
     * Refresh notifications on user sigin.
     *
     * Ex: then user signin and configuration is applying at this time - it's
     * need to notify user about this action.
     */
    private void refreshNotifications() {
        notificationService.sendNotification(new ApplyConfigurationNotificationEvent());
    }

    private void updateContent() {
        DbUser user = userAppService.getUser();
        if (user != null) {
            mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
            mainView.initializeUserMenu(user.getLogin(), new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());
            notificationService.addSubscriber(mainView.getBtnNotifications());
            mainView.getSideBarMenu().select(0);

            refreshNotifications();
            setContent(mainView);
        } else {
            loginView.initialize();
            setContent(loginView);
        }
    }

    @Override
    public void onServerResponse(final SuccessResponse response, final Map<String, Object> data) {
        if (response instanceof AbstractAuthCommandResponse) {
            if (response instanceof AuthSuccessComandResponse) {
                Notification.show("Notification", "onServerResponse, AuthSuccessComandResponse", Notification.Type.WARNING_MESSAGE);
                try {
                    UserDto userDto = ((AuthSuccessComandResponse) response).getUser();
                    if (userDto != null) {

                        boolean isRememberMe = false;

                        DbUser user = userService.findUserByLogin(userDto.getUsername(), true);
                        if (user == null) {
                            throw new Exception("Error authentication/authorization user");
                        }

                        DbConfiguration config = configurationService.findConfigurationByUser(user, true);
                        if (config == null) {
                            throw new Exception("Error authentication/authorization user");
                        }

                        user.setPassword(userDto.getPassword());
                        user.setToken(userDto.getUserToken());

                        //set params to session
                        userAppService.setUser(user);
                        userAppService.setConfiguration(config);
                        settingsAppService.setServerConfigPath(userDto.getSettings() == null ? "" : userDto.getSettings().getConfigPath());

                        if (data != null) {
                            isRememberMe = data.get(Attributes.REMEMBER_ME.value()) == null ? false : (Boolean) data.get(Attributes.REMEMBER_ME.value());
                        }

                        if (isRememberMe) {
                            Cookie cookie = new Cookie(Attributes.REMEMBER_ME.value(), user.getToken());
                            cookie.setMaxAge(Constants.REMEMBER_ME_COOKIE_MAX_AGE);
                            //#hack cookie
                            UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
                        } else {
                            Cookie cookie = new Cookie(Attributes.REMEMBER_ME.value(), "");
                            cookie.setMaxAge(0);
                            //#hack cookie
                            UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
                        }

                        updateContent();
                    } else {
                        throw new Exception("Error authentication/authorization user");
                    }
                } catch (Exception ex) {
                    log.error("Error sign in", ex);
                    Notification.show("Notification", "Error authentication/authorization user", Notification.Type.WARNING_MESSAGE);
                }
            } else if (response instanceof AuthBadCredentialsCommandResponse) {
                Notification.show("Sign In", "Error authentication/authorization user", Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    @Override
    public void onServerResponse(final SuccessResponse response) {
        throw new UnsupportedOperationException("Method is not implemented");
    }

    @Override
    public void onServerException(final ExceptionResponse exception) {
        Notification.show("Notification", "onServerException", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void onLocalException(Exception ex) {
        Notification.show("Notification", "onLocalException", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void onTimeout() {
        Notification.show("Notification", "onTimeout", Notification.Type.WARNING_MESSAGE);
    }
}
