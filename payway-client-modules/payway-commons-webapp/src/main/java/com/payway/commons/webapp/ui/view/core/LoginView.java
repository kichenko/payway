/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.payway.commons.webapp.core.Attributes;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.ResponseCallBack;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.bus.events.LoginExceptionSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginFailSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.validator.Validator;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.response.auth.AbstractAuthCommandResponse;
import com.payway.messaging.message.response.auth.AuthBadCredentialsCommandResponse;
import com.payway.messaging.message.response.auth.AuthSuccessCommandResponse;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * LoginView
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Slf4j
@UIScope
@Component
public class LoginView extends AbstractCustomComponentView implements ResponseCallBack<SuccessResponse, ExceptionResponse> {

    private static final long serialVersionUID = -8709373681721076425L;

    @Autowired
    @Qualifier("serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("messageServerSenderService")
    private MessageServerSenderService service;

    @Autowired
    @Qualifier(value = "sessionEventBus")
    private SessionEventBus sessionEventBus;

    @Autowired
    @Qualifier("userNameValidator")
    private Validator userNameValidator;

    @Autowired
    @Qualifier("userPasswordValidator")
    private Validator userPasswordValidator;

    @UiField
    private TextField editUserName;

    @UiField
    private PasswordField editPassword;

    @UiField
    private Button buttonSignIn;

    @UiField
    private CheckBox checkBoxRememberMe;

    @UiField
    private Label labelTitle;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        setCompositionRoot(Clara.create("LoginView.xml", this));
        init();
    }

    private void init() {

        editUserName.addShortcutListener(new ShortcutListener("Sign in (Enter)", ShortcutAction.KeyCode.ENTER, null) {
            private static final long serialVersionUID = -7690864248678996551L;

            @Override
            public void handleAction(Object sender, Object target) {
                buttonSignIn.click();
            }
        });

        editPassword.addShortcutListener(new ShortcutListener("Sign in (Enter)", ShortcutAction.KeyCode.ENTER, null) {
            private static final long serialVersionUID = -7690864248678996551L;

            @Override
            public void handleAction(Object sender, Object target) {
                buttonSignIn.click();
            }
        });
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

    public void setTitle(String title) {
        labelTitle.setValue(title);
    }

    public boolean isRememberMe() {
        return checkBoxRememberMe.getValue();
    }

    @UiHandler("buttonSignIn")
    public void clickButtonSignIn(Button.ClickEvent event) throws Exception {

        if (!userNameValidator.validate(editUserName.getValue())) {
            editUserName.setComponentError(new UserError("Invalid username, please correct"));
            return;
        }

        if (!userPasswordValidator.validate(editPassword.getValue())) {
            editPassword.setComponentError(new UserError("Invalid password, please correct"));
            return;
        }

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        serverTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                service.auth(editUserName.getValue(), editPassword.getValue(), LoginView.this);
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
                    if (response instanceof AbstractAuthCommandResponse) {
                        if (response instanceof AuthSuccessCommandResponse) {
                            sessionEventBus.sendNotification(new LoginSuccessSessionBusEvent(((AuthSuccessCommandResponse) response).getUser()));
                        } else if (response instanceof AuthBadCredentialsCommandResponse) {
                            sessionEventBus.sendNotification(new LoginFailSessionBusEvent());
                        }
                    } else {
                        log.error("Bad auth server response (unknown type) - {}", response);
                        sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
                    }
                }
            });
        }
    }

    @Override
    public void onServerException(final ExceptionResponse ex) {
        UI ui = getUI();
        if (ui != null) {
            ui.access(new Runnable() {
                @Override
                public void run() {
                    log.error("Bad auth server response (server exception) - {}", ex);
                    sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
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
                    log.error("Bad auth server response (local exception) - {}", ex);
                    sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
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
                    log.error("Bad auth server response (timeout)");
                    sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
                }
            });
        }
    }
}
