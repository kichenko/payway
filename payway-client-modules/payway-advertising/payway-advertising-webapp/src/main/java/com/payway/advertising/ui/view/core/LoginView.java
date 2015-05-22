/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.messaging.MessageServerSenderService;
import com.payway.advertising.messaging.ResponseCallBack;
import com.payway.advertising.ui.component.ProgressBarWindow;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

/**
 * Логин
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@UIScope
@Component
public class LoginView extends AbstractCustomComponentView implements ResponseCallBack<SuccessResponse, ExceptionResponse> {

    @Autowired
    @Qualifier("serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("messageServerSenderService")
    MessageServerSenderService service;

    @UiField
    private TextField textUserName;

    @UiField
    private PasswordField textPassword;

    @UiField
    private CheckBox checkBoxRememberMe;

    private final ProgressBarWindow progressBarWindow = new ProgressBarWindow();

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        setCompositionRoot(Clara.create("LoginView.xml", this));
    }

    @Override
    public void initialize() {
        Cookie rememberMeCookie = getCookieByName(Attributes.REMEMBER_ME.value());
        if (rememberMeCookie != null && rememberMeCookie.getValue() != null) {
            checkBoxRememberMe.setValue(true);
        } else {
            checkBoxRememberMe.setValue(false);
        }
    }

    @UiHandler("buttonSignIn")
    public void clickButtonSignIn(Button.ClickEvent event) throws Exception {
        progressBarWindow.show();
        serverTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                service.auth(textUserName.getValue(), textPassword.getValue(), LoginView.this);
            }
        });
    }

    @Override
    public void onServerResponse(final SuccessResponse response) {
        final UI ui = getUI();
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    progressBarWindow.close();
                    Map<String, Object> map = new HashMap<>();
                    map.put(Attributes.REMEMBER_ME.value(), checkBoxRememberMe.getValue());
                    ((ResponseCallBack) UI.getCurrent()).onServerResponse(response, map);
                }
            });
        }
    }

    @Override
    public void onServerException(final ExceptionResponse exception) {
        UI ui = getUI();
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    progressBarWindow.close();
                    ((ResponseCallBack) UI.getCurrent()).onServerException(exception);
                }
            });
        }
    }

    @Override
    public void onLocalException(final Exception ex) {
        final UI ui = getUI();
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    progressBarWindow.close();
                    ((ResponseCallBack) UI.getCurrent()).onLocalException(ex);
                }
            });
        }
    }

    @Override
    public void onTimeout() {
        final UI ui = getUI();
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    progressBarWindow.close();
                    ((ResponseCallBack) UI.getCurrent()).onTimeout();
                }
            });
        }
    }

    @Override
    public void onServerResponse(SuccessResponse response, Map<String, Object> data) {
        throw new UnsupportedOperationException("Method is not implemented");
    }

}
