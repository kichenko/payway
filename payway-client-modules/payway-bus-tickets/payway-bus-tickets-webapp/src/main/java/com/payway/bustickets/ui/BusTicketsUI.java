/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.bustickets.service.app.AppService;
import com.payway.bustickets.ui.bus.events.BusTicketOperatorsFailBusEvent;
import com.payway.bustickets.ui.bus.events.BusTicketOperatorsSuccessBusEvent;
import com.payway.commons.webapp.core.Attributes;
import com.payway.commons.webapp.core.Constants;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.ResponseCallBack;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.bus.events.LoginExceptionSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginFailSessionBusEvent;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.LoginView;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.bustickets.BusTicketOperatorsRequest;
import com.payway.messaging.message.bustickets.BusTicketOperatorsResponse;
import com.payway.messaging.model.common.OperatorDto;
import com.payway.messaging.model.message.auth.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * BusTicketsUI
 *
 * @author Sergey Kichenko
 * @created 04.06.15 00:00
 */
@Slf4j
@SpringUI
@Theme("default")
@PreserveOnRefresh
@Widgetset("com.payway.bustickets.BusTicketsWidgetSet")
public class BusTicketsUI extends AbstractUI {

    private static final long serialVersionUID = 1197507013599612841L;

    @Getter(onMethod = @_({
        @Override}))
    @Autowired
    @Qualifier(value = "sessionEventBus")
    private SessionEventBus sessionEventBus;

    @Autowired
    @Qualifier(value = "appService")
    private AppService appService;

    @Autowired
    private AbstractMainView mainView;

    @Autowired
    private LoginView loginView;

    @Autowired
    @Qualifier("messageServerSenderService")
    private MessageServerSenderService service;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        updateContent();

        addDetachListener(new DetachListener() {
            private static final long serialVersionUID = -327258454602850406L;

            @Override
            public void detach(DetachEvent event) {
                cleanUp();
            }
        });

        sessionEventBus.addSubscriber(this);
    }

    private void cleanUp() {
        sessionEventBus.removeSubscriber(this);
    }

    @Override
    protected Collection<SideBarMenu.MenuItem> getSideBarMenuItems() {

        List<OperatorDto> operators = appService.getUserBusTicketOperators();
        SideBarMenu.MenuItem menu = new SideBarMenu.MenuItem("bus-ticket-empty-workspace-view", "Bus Tickets", new ThemeResource("images/sidebar_bus_tickets.png"), null);

        if (operators != null) {
            List<SideBarMenu.MenuItem> childs = new LinkedList<>();
            for (OperatorDto operator : operators) {
                //STALWART <-> airport-express-bus-tickets-workspace-view
                String workspaceViewName = appService.getBusTicketOperatorWorkspaceViewName(operator.getShortName());
                if (!StringUtils.isBlank(workspaceViewName)) {
                    childs.add(new SideBarMenu.MenuItem(workspaceViewName, operator.getName(), new ThemeResource("images/sidebar_bus_tickets.png"), null));
                }
            }
            menu.setChilds(childs);
        }

        return Collections.singletonList(menu);
    }

    private void sendBusTicketOperatorsRequest() {
        service.sendMessage(new BusTicketOperatorsRequest(), new ResponseCallBack() {

            @Override
            public void onServerResponse(final SuccessResponse response) {
                UI ui = getUI();
                if (ui != null) {
                    ui.access(new Runnable() {
                        @Override
                        public void run() {
                            if (response instanceof BusTicketOperatorsResponse) {
                                sessionEventBus.sendNotification(new BusTicketOperatorsSuccessBusEvent(((BusTicketOperatorsResponse) response).getOperators()));
                            } else {
                                log.error("Bad bus tickets operators server response (unknown type) - {}", response);
                                sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
                            }
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
                            log.error("Bad bus tickets operators server response (server exception) - {}", exception);
                            sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
                        }
                    });
                }
            }

            @Override
            public void onLocalException(final Exception exception) {
                UI ui = getUI();
                if (ui != null) {
                    ui.access(new Runnable() {
                        @Override
                        public void run() {
                            log.error("Bad bus tickets operators server response (local exception) - {}", exception);
                            sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
                        }
                    });
                }
            }

            @Override
            public void onTimeout() {
                UI ui = getUI();
                if (ui != null) {
                    ui.access(new Runnable() {
                        @Override
                        public void run() {
                            log.error("Bad bus tickets operators server response (timeout exception)");
                            sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
                        }
                    });
                }
            }
        });
    }

    private void updateContent() {

        //mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
        //mainView.initializeUserMenu("", new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());
        //mainView.getSideBarMenu().select(0);
        //setContent(mainView);
        UserDto user = appService.getUser();
        if (user != null) {
            mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
            mainView.initializeUserMenu(user.getUsername(), new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());
            mainView.getSideBarMenu().select(0);

            setContent(mainView);
        } else {
            loginView.setTitle("Payway BusTickets Desktop");
            loginView.initialize();
            setContent(loginView);
        }
    }

    @Subscribe
    public void processSessionBusEvent(LoginFailSessionBusEvent event) {
        log.error("Bad user sign in (bad auth)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void processSessionBusEvent(LoginExceptionSessionBusEvent event) {
        log.error("Bad user sign in (exception)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void processSessionBusEvent(LoginSuccessSessionBusEvent event) {

        try {

            UserDto user = event.getUser();
            if (user == null) {
                throw new Exception("User sign in");
            }

            //set params to session
            appService.setUser(event.getUser());

            if (loginView.isRememberMe()) {
                Cookie cookie = new Cookie(Attributes.REMEMBER_ME.value(), user.getUserToken());
                cookie.setMaxAge(Constants.REMEMBER_ME_COOKIE_MAX_AGE);
                //#hack cookie
                UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
            } else {
                Cookie cookie = new Cookie(Attributes.REMEMBER_ME.value(), "");
                cookie.setMaxAge(0);
                //#hack cookie
                UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + cookie.getName() + "=" + cookie.getValue() + "; path=/'; expires=" + cookie.getMaxAge());
            }

            //send request to get bus ticket operators
            sendBusTicketOperatorsRequest();

        } catch (Exception ex) {
            log.error("Bad user sign in", ex);
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
        }
    }

    @Subscribe
    public void processSessionBusEvent(BusTicketOperatorsFailBusEvent event) {
        log.error("Bad bus tickets operators server response (exception)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad bus tickets operators server response", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void processSessionBusEvent(BusTicketOperatorsSuccessBusEvent event) {

        //set params to session
        appService.setUserBusTicketOperators(event.getOperators());

        updateContent();
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }
}
