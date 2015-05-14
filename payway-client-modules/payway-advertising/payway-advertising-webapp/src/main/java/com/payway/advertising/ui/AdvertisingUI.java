/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.payway.advertising.core.service.user.UserService;
import com.payway.advertising.messaging.ResponseCallBack;
import com.payway.advertising.model.User;
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
import com.payway.messaging.model.messaging.auth.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
public class AdvertisingUI extends UI implements ResponseCallBack<SuccessResponse, ExceptionResponse> {

    @Autowired
    private MainView mainView;

    @Autowired
    private LoginView loginView;

    @Autowired
    @Qualifier(value = "userService")
    private UserService userService;

    @Override
    protected void init(VaadinRequest request) {
        updateContent();
    }

    private Collection<SideBarMenu.MenuItem> getSideBarMenuItems() {
        Collection<SideBarMenu.MenuItem> items = new ArrayList<>(5);
        items.add(new SideBarMenu.MenuItem("content-configuration", "Configuration", FontAwesome.HOME));
        items.add(new SideBarMenu.MenuItem("reports", "Reports", FontAwesome.BAR_CHART_O));
        return items;
    }

    private Collection<ImmutablePair<String, MenuBar.Command>> getMenuBarItems() {
        return Collections.singletonList(
                new ImmutablePair<String, MenuBar.Command>(
                        "Sign Out", new MenuBar.Command() {
                            @Override
                            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                                VaadinSession.getCurrent().close();
                                UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
                                VaadinSession.getCurrent().close();
                                Page.getCurrent().reload();
                            }
                        }));
    }

    private void updateContent() {
        User user = userService.getUser();
        if (user != null) {
            mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
            mainView.initializeUserMenu(user.getUserName(), getMenuBarItems());
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

                UserDto userDto = ((AuthSuccessComandResponse) response).getUser();
                if (userDto != null && userService.setUser(new User(userDto.getUsername(), userDto.getPassword(), userDto.getUserToken()))) {

                    boolean isRememberMe = false;
                    User user = userService.getUser();

                    if (data != null) {
                        isRememberMe = data.get(Attributes.REMEMBER_ME.value()) == null ? false : (Boolean) data.get(Attributes.REMEMBER_ME.value());
                    }

                    if (isRememberMe) {
                        Cookie cookie = new Cookie(Attributes.REMEMBER_ME.value(), user.getToken());
                        cookie.setMaxAge(Constants.REMEMBER_ME_COOKIE_MAX_AGE);
                        //#hack cookie
                        UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
                        //VaadinService.getCurrentResponse().addCookie(cookie);
                    } else {
                        Cookie cookie = new Cookie(Attributes.REMEMBER_ME.value(), "");
                        cookie.setMaxAge(0);
                        //#hack cookie
                        UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
                        //VaadinService.getCurrentResponse().addCookie(cookie);
                    }
                    updateContent();
                } else {
                    Notification.show("Notification", "Failed to get session authentication/authorization user", Notification.Type.WARNING_MESSAGE);
                    log.error("Failed to get session authentication/authorization user");
                }
            } else if (response instanceof AuthBadCredentialsCommandResponse) {
                Notification.show("Sign In", "Invalid username or password", Notification.Type.WARNING_MESSAGE);
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
    public void onLocalException() {
        Notification.show("Notification", "onLocalException", Notification.Type.WARNING_MESSAGE);
    }

    @Override
    public void onTimeout() {
        Notification.show("Notification", "onTimeout", Notification.Type.WARNING_MESSAGE);
    }
}
