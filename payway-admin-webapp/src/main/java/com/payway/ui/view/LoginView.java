/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * LoginView of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
public final class LoginView extends CustomComponent {

    @UiField
    private Button buttonSignIn;

    public LoginView() {
        setSizeFull();
        setCompositionRoot(Clara.create("LoginView.xml", this));
    }

    @UiHandler("buttonSignIn")
    public void clickButtonSignIn(Button.ClickEvent event) {
        Notification.show("Notification", "Not implemented", Notification.Type.WARNING_MESSAGE);
    }
}
