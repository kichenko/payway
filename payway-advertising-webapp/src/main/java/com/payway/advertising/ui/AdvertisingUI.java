/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.payway.advertising.messaging.ResponseCallBack;
import com.payway.advertising.ui.core.AdvertisingSessionAttributeType;
import com.payway.advertising.ui.core.LoginView;
import com.payway.advertising.ui.core.MainView;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.response.auth.AbstractAuthCommandResponse;
import com.payway.messaging.message.response.auth.AuthBadCredentialsCommandResponse;
import com.payway.messaging.message.response.auth.AuthSuccessComandResponse;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Главное UI
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@SpringUI
@Slf4j
@Theme("default")
@Widgetset("com.payway.advertising.AdvertisingWidgetSet")
public class AdvertisingUI extends UI implements ResponseCallBack<SuccessResponse, ExceptionResponse> {

    @Autowired
    private MainView mainView;

    @Autowired
    private LoginView loginView;

    @Override
    protected void init(VaadinRequest request) {
        updateContent();
    }

    private void updateContent() {
        if (VaadinSession.getCurrent().getAttribute(AdvertisingSessionAttributeType.USER.value()) != null) {
            setContent(mainView);
            mainView.initializeSideBarMenu();
            mainView.initializeUserMenu();
        } else {
            if (loginView != null) {
                loginView.init();
                setContent(loginView);
            }
        }
    }

    @Override
    public void onServerResponse(final SuccessResponse response) {
        if (response instanceof AbstractAuthCommandResponse) {
            if (response instanceof AuthSuccessComandResponse) {
                Notification.show("Notification", "onServerResponse, AuthSuccessComandResponse", Notification.Type.WARNING_MESSAGE);

                VaadinSession session = VaadinSession.getCurrent();
                if (session != null) {
                    session.setAttribute(AdvertisingSessionAttributeType.USER.value(), ((AuthSuccessComandResponse) response).getUser());

                    //VaadinService.getCurrentResponse().
                    updateContent();
                } else {
                    Notification.show("Notification", "Failed to get session authentication/authorization user", Notification.Type.WARNING_MESSAGE);
                    log.error("Failed to get session authentication/authorization user");
                }
            } else if (response instanceof AuthBadCredentialsCommandResponse) {
                Notification.show("Notification", "onServerResponse, AuthBadCredentialsCommandResponse", Notification.Type.WARNING_MESSAGE);
            }
        }
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
