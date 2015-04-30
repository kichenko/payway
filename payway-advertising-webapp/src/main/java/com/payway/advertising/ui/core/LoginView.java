/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.core;

import com.payway.advertising.messaging.MessageServerSenderServiceImpl;
import com.payway.advertising.messaging.ResponseCallBack;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.request.auth.AuthCommandRequest;
import com.payway.model.messaging.auth.UserImpl;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * Логин
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginView extends CustomComponent implements ResponseCallBack<SuccessResponse, ExceptionResponse> {

    @Autowired
    @Qualifier("serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("serviceSender")
    MessageServerSenderServiceImpl service;

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
    
    public void init() {
        //
    }

    @UiHandler("buttonSignIn")
    public void clickButtonSignIn(Button.ClickEvent event) throws Exception {
        progressBarWindow.show();

        if (checkBoxRememberMe.getValue()) {
            Cookie cookie = new Cookie("remember-me", "");
            cookie.setMaxAge(120);
            VaadinService.getCurrentResponse().addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("remember-me", "");
            cookie.setMaxAge(0);
            VaadinService.getCurrentResponse().addCookie(cookie);
        }

        serverTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                service.sendMessage(new AuthCommandRequest<>(new UserImpl(textUserName.getValue(), textPassword.getValue(), "", checkBoxRememberMe.getValue(), null), true), LoginView.this);
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
                    ((ResponseCallBack) UI.getCurrent()).onServerResponse(response);
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
    public void onLocalException() {
        final UI ui = getUI();
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    progressBarWindow.close();
                    ((ResponseCallBack) UI.getCurrent()).onLocalException();
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
}
