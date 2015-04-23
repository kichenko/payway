/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui.core;

import com.payway.admin.core.service.event.AdminEventBusService;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import lombok.NoArgsConstructor;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * LoginView of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@NoArgsConstructor
public final class LoginView extends CustomComponentView {

    @UiField
    private TextField textUserName;

    @UiField
    private TextField textPassword;

    @UiField
    private CheckBox checkBoxRememberMe;

    private final ProgressBarWindow progressBarWindow = new ProgressBarWindow();

    public LoginView(AdminEventBusService adminEventBusService) {
        super(adminEventBusService);

        setSizeFull();
        setCompositionRoot(Clara.create("LoginView.xml", this));
    }

    @UiHandler("buttonSignIn")
    public void clickButtonSignIn(Button.ClickEvent event) {
        progressBarWindow.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    //
                }

                UI.getCurrent().access(new Runnable() {
                    @Override
                    public void run() {
                        progressBarWindow.close();
                    }
                });

            }
        }).start();

        //if (getAdminEventBusService() != null) {
        //    getAdminEventBusService().post(new UserSignInBusEvent(textUserName.getValue(), textPassword.getValue(), checkBoxRememberMe.getValue()));
        //}
    }
}
