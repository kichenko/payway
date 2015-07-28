/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.service.app.user.WebAppUser;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.bus.events.LoginExceptionSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginFailSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginRememberMeFailSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.LoginView;
import com.payway.commons.webapp.utils.WebAppUtils;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.response.auth.AuthSuccessCommandResponse;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;

/**
 * AbstractLoginUI
 *
 * @author Sergey Kichenko
 * @created 17.07.15 00:00
 */
@Slf4j
public abstract class AbstractLoginUI extends AbstractUI {

    private static final long serialVersionUID = 1549140715202605066L;

    @Getter
    @Setter
    @Autowired
    protected LoginView loginView;

    @Autowired
    protected WebAppUserService webAppUserService;

    @Autowired
    protected MessageServerSenderService service;

    @Autowired
    protected TaskExecutor serverTaskExecutor;

    @Value("${app.auth.token.expired.days:7}")
    protected int authTokenExpiredDays;

    protected abstract String getLoginTitle();

    protected abstract void setupWorkspaceContent();

    protected void setupWebApp(VaadinRequest request) {
        //
    }

    @Override
    protected void init(VaadinRequest request) {

        registerDetach();
        subscribeSessionEventBus();
        setupWebApp(request);

        setupUIContent();
    }

    protected void setupLoginContent() {

        loginView.setTitle(getLoginTitle());
        loginView.initialize();
        setContent(loginView);
    }

    protected void setupRememberMeContent() {

        Cookie cookie = getCookieByName(CommonAttributes.REMEMBER_ME.value());
        if (cookie == null) {
            setupLoginContent();
        } else {

            final UI ui = UI.getCurrent();
            final String sessionId = cookie.getValue();
            final String clientIpAddress = WebAppUtils.getRemoteAddress(VaadinService.getCurrentRequest(), Page.getCurrent().getWebBrowser());

            if (log.isDebugEnabled()) {
                log.debug("Try to sign in with remote ip address - [{}]", clientIpAddress);
            }

            ((InteractionUI) UI.getCurrent()).showProgressBar();
            serverTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    //TODO: need send auth with sessionId only
                    service.auth(sessionId, clientIpAddress, new UIResponseCallBackSupport(ui, new UIResponseCallBackSupport.ResponseCallBackHandler() {

                        @Override
                        public void doServerResponse(SuccessResponse response) {

                            SessionEventBus sessionEventBus = ((AbstractUI) UI.getCurrent()).getSessionEventBus();
                            if (response instanceof AuthSuccessCommandResponse) {
                                AuthSuccessCommandResponse rsp = (AuthSuccessCommandResponse) response;

                                //#hack cookie
                                if (((AbstractLoginUI) UI.getCurrent()).getLoginView().isRememberMe()) {
                                    URI uri = UI.getCurrent().getPage().getLocation();
                                    UI.getCurrent().getPage().getJavaScript().execute("var date = new Date(); date.setTime(date.getTime()+(" + Integer.toString(authTokenExpiredDays) + "*24*60*60*1000)); document.cookie='" + CommonAttributes.REMEMBER_ME.value() + "=" + rsp.getSessionId() + "; path=" + uri.getPath() + "; expires=' + date.toGMTString();");
                                }

                                sessionEventBus.sendNotification(new LoginSuccessSessionBusEvent(rsp.getUser(), rsp.getSessionId(), rsp.getExtensions()));
                            } else {
                                sessionEventBus.sendNotification(new LoginRememberMeFailSessionBusEvent());
                            }
                        }

                        @Override
                        public void doServerException(ExceptionResponse ex) {
                            log.error("Bad auth server response (server exception) - {}", ex);
                            ((AbstractUI) UI.getCurrent()).getSessionEventBus().sendNotification(new LoginRememberMeFailSessionBusEvent());
                        }

                        @Override
                        public void doLocalException(Exception ex) {
                            log.error("Bad auth server response (local exception) - {}", ex);
                            ((AbstractUI) UI.getCurrent()).getSessionEventBus().sendNotification(new LoginRememberMeFailSessionBusEvent());
                        }

                        @Override
                        public void doTimeout() {
                            log.error("Bad auth server response (timeout)");
                            ((AbstractUI) UI.getCurrent()).getSessionEventBus().sendNotification(new LoginRememberMeFailSessionBusEvent());
                        }
                    }));
                }
            });
        }
    }

    @Override
    protected List<AbstractMainView.UserMenuItem> getMenuBarItems() {
        return Collections.singletonList(
                new AbstractMainView.UserMenuItem("Sign Out", new ThemeResource("images/user_menu_item_logout.png"), new MenuBar.Command() {
                    private static final long serialVersionUID = 7160936162824727503L;

                    @Override
                    public void menuSelected(final MenuBar.MenuItem selectedItem) {
                        for (final UI ui : VaadinSession.getCurrent().getUIs()) {
                            ui.access(new Runnable() {
                                @Override
                                public void run() {
                                    //#hack cookie
                                    URI uri = UI.getCurrent().getPage().getLocation();
                                    UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + CommonAttributes.REMEMBER_ME.value() + "=;" + "; path=" + uri.getPath() + " ;expires=Thu, 01-Jan-1970 00:00:01 GMT;'");
                                    ui.getPage().reload();
                                }
                            });
                        }
                        VaadinSession.getCurrent().close();
                    }
                }, false));
    }

    protected void setupUIContent() {

        WebAppUser user = webAppUserService.getUser();
        if (user != null) {
            setupWorkspaceContent();
        } else {
            //TODO: now is not implemented
            setupRememberMeContent();
        }
    }

    @Subscribe
    public void onLoginFail(LoginFailSessionBusEvent event) {
        log.error("Bad user sign in (bad auth)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void onLoginException(LoginExceptionSessionBusEvent event) {
        log.error("Bad user sign in (exception)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void onLoginRememberMeFail(LoginRememberMeFailSessionBusEvent event) {
        log.error("Bad user sign in with remember me");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        setupLoginContent();
    }
}
