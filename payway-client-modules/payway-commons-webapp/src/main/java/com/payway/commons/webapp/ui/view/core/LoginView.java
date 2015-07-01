/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.bus.events.LoginExceptionSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginFailSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.utils.WebAppUtils;
import com.payway.commons.webapp.validator.Validator;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.response.auth.AbstractAuthCommandResponse;
import com.payway.messaging.message.response.auth.AuthBadCredentialsCommandResponse;
import com.payway.messaging.message.response.auth.AuthSuccessCommandResponse;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
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
 * LoginView
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginView extends CustomComponent implements CustomComponentInitialize, UIResponseCallBackSupport.ResponseCallBackHandler {

    private static final long serialVersionUID = -8709373681721076425L;

    @Autowired
    @Qualifier("serverTaskExecutor")
    private TaskExecutor serverTaskExecutor;

    @Autowired
    @Qualifier("messageServerSenderService")
    private MessageServerSenderService service;

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
        init();
    }

    private void init() {

        setCompositionRoot(Clara.create("LoginView.xml", this));

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

        Cookie rememberMeCookie = getCookieByName(CommonAttributes.REMEMBER_ME.value());
        if (rememberMeCookie != null && rememberMeCookie.getValue() != null) {
            checkBoxRememberMe.setValue(true);
        } else {
            checkBoxRememberMe.setValue(false);
        }
    }

    protected Cookie getCookieByName(String name) {

        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
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
            editUserName.setComponentError(new UserError("Invalid user name"));
            return;
        }

        if (!userPasswordValidator.validate(editPassword.getValue())) {
            editPassword.setComponentError(new UserError("Invalid password"));
            return;
        }

        final UI ui = getUI();
        final String userName = editUserName.getValue();
        final String userPassword = editPassword.getValue();
        final String clientIpAddress = WebAppUtils.getRemoteAddress(VaadinService.getCurrentRequest(), Page.getCurrent().getWebBrowser());

        if (log.isDebugEnabled()) {
            log.debug("Try to sign in with remote ip address - [{}]", clientIpAddress);
        }

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        serverTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                service.auth(userName, userPassword, clientIpAddress, new UIResponseCallBackSupport(ui, LoginView.this));
            }
        });
    }

    private SessionEventBus popSessionEventBus(final UI ui) {

        if (!(ui instanceof AbstractUI)) {
            log.error("UI is not instance of {}, could not get session event bus", AbstractUI.class.getName());
            return null;
        }

        return ((AbstractUI) ui).getSessionEventBus();
    }

    @Override
    public void doServerResponse(final SuccessResponse response) {

        SessionEventBus sessionEventBus = popSessionEventBus(UI.getCurrent());
        if (sessionEventBus == null) {
            log.error("Could not pop session event bus from ui on sucess server response");
            return;
        }

        if (!(response instanceof AbstractAuthCommandResponse)) {
            log.error("Bad auth server response (unknown type) - {}", response);
            sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
            return;
        }

        if (response instanceof AuthSuccessCommandResponse) {
            AuthSuccessCommandResponse rsp = (AuthSuccessCommandResponse) response;
            sessionEventBus.sendNotification(new LoginSuccessSessionBusEvent(rsp.getUser(), rsp.getSessionId(), rsp.getExtensions()));
        } else if (response instanceof AuthBadCredentialsCommandResponse) {
            sessionEventBus.sendNotification(new LoginFailSessionBusEvent());
        }
    }

    @Override
    public void doServerException(final ExceptionResponse ex) {

        SessionEventBus sessionEventBus = popSessionEventBus(UI.getCurrent());
        if (sessionEventBus == null) {
            log.error("Could not pop session event bus from ui on server exception response");
            return;
        }

        log.error("Bad auth server response (server exception) - {}", ex);
        sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
    }

    @Override
    public void doLocalException(final Exception ex) {

        SessionEventBus sessionEventBus = popSessionEventBus(UI.getCurrent());
        if (sessionEventBus == null) {
            log.error("Could not pop session event bus from ui on local server exception response");
            return;
        }

        log.error("Bad auth server response (local exception) - {}", ex);
        sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
    }

    @Override
    public void doTimeout() {

        SessionEventBus sessionEventBus = popSessionEventBus(UI.getCurrent());
        if (sessionEventBus == null) {
            log.error("Could not pop session event bus from ui on server response timeout");
            return;
        }

        log.error("Bad auth server response (timeout)");
        sessionEventBus.sendNotification(new LoginExceptionSessionBusEvent());
    }
}
