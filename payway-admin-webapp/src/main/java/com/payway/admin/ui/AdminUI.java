/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui;

import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.payway.admin.core.event.SideBarMenuItemClickBusEvent;
import com.payway.admin.core.event.UserSignInBusEvent;
import com.payway.admin.core.event.UserSignOutBusEvent;
import com.payway.admin.core.service.event.AdminEventBusService;
import com.payway.admin.ui.core.LoginView;
import com.payway.admin.ui.core.MainView;
import com.payway.messaging.core.ResponseEnvelope;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UI viewport of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@SpringUI
@Theme("default")
@NoArgsConstructor
@Widgetset("com.payway.admin.AdminWidgetSet")
public final class AdminUI extends UI implements BroadcastResponseListener {

    @Getter
    @Autowired
    private AdminEventBusService adminEventBusService;

    private boolean isAuth = false;

    @Override
    protected void init(VaadinRequest request) {
        adminEventBusService.register(this);
        updateContent();
    }

    private void updateContent() {
        if (isAuth) {
            setContent(new MainView(getAdminEventBusService()));
        } else {
            setContent(new LoginView(getAdminEventBusService()));
        }
    }

    /**
     * Handle user sign in bus event
     *
     * @param event user sign in bus event
     */
    @Subscribe
    public void handleUserSignInBusEvent(final UserSignInBusEvent event) {
        isAuth = true;
        updateContent();
    }

    /**
     * Handle user sign out bus event
     *
     * @param event user sign out bus event
     */
    @Subscribe
    public void handleUserSignOutBusEvent(final UserSignOutBusEvent event) {
        Notification.show("Notification", "Subscribe UserSignOutBusEvent", Notification.Type.WARNING_MESSAGE);
    }

    /**
     * Handle sidebar menu item click bus event
     *
     * @param event sidebar menu item click bus event
     */
    @Subscribe
    public void handleSideBarMenuItemClickBusEvent(final SideBarMenuItemClickBusEvent event) {
        Notification.show("Notification", "Subscribe SideBarMenuItemClickBusEvent", Notification.Type.WARNING_MESSAGE);
        if (getNavigator() != null) {
            getNavigator().navigateTo(event.getTag());
        }
    }

    /**
     * Обработка ответов от сервера. Код выполняется в разных потоках.
     *
     * @param envelope пакет с сообщением
     */
    @Override
    public void receiveResponse(ResponseEnvelope envelope) {
        //
    }
}
