/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.bustickets.service.app.settings.model.BusTicketsSessionSettings;
import com.payway.bustickets.ui.bus.events.BusTicketOperatorsFailBusEvent;
import com.payway.bustickets.ui.bus.events.BusTicketOperatorsSuccessBusEvent;
import com.payway.bustickets.ui.view.core.BusTicketsSettingsWindow;
import com.payway.bustickets.ui.view.workspace.BusTicketsEmptyWorkspaceView;
import com.payway.bustickets.ui.view.workspace.BusTicketsWorkspaceView;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.service.app.settings.SettingsAppService;
import com.payway.commons.webapp.service.app.user.WebAppUser;
import com.payway.commons.webapp.ui.AbstractLoginUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.bustickets.BusTicketOperatorsRequest;
import com.payway.messaging.message.bustickets.BusTicketOperatorsResponse;
import com.payway.messaging.model.common.OperatorDto;
import com.payway.messaging.model.common.RetailerTerminalDto;
import com.payway.messaging.model.common.RetailerTerminalsDto;
import com.payway.messaging.model.user.UserDto;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
public class BusTicketsUI extends AbstractLoginUI {

    private static final long serialVersionUID = 1197507013599612841L;

    @Autowired
    private AbstractMainView mainView;

    @Autowired
    private SettingsAppService<BusTicketsSessionSettings> settingsAppService;

    @Override
    protected List<SideBarMenu.MenuItem> getSideBarMenuItems() {

        List<OperatorDto> operators = settingsAppService.getSessionSettings().getOperators();
        SideBarMenu.MenuItem menu = new SideBarMenu.MenuItem("bus-tickets-empty", BusTicketsEmptyWorkspaceView.BUS_TICKETS_EMPTY_WORKSPACE_VIEW_ID, "Bus Tickets", new ThemeResource("images/sidebar_bus_tickets.png"), null, null);

        if (operators != null) {
            List<SideBarMenu.MenuItem> childs = new LinkedList<>();
            for (OperatorDto operator : operators) {
                if (!StringUtils.isBlank(operator.getShortName())) {
                    childs.add(new SideBarMenu.MenuItem(operator.getShortName(), BusTicketsWorkspaceView.BUS_TICKETS_WORKSPACE_VIEW_ID, operator.getName(), new ThemeResource("images/sidebar_bus_tickets.png"), operator.getId(), null));
                }
            }

            menu.setChilds(childs);
        }

        return Collections.singletonList(menu);
    }

    private void sendBusTicketOperatorsRequest() {

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        service.sendMessage(new BusTicketOperatorsRequest(webAppUserService.getUser().getSessionId(), settingsAppService.getSessionSettings().getCurrentRetailerTerminal().getId()), new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {
                if (response instanceof BusTicketOperatorsResponse) {
                    sessionEventBus.sendNotification(new BusTicketOperatorsSuccessBusEvent(((BusTicketOperatorsResponse) response).getOperators()));
                } else {
                    log.error("Bad bus tickets operators server response (unknown type) - {}", response);
                    sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
                }
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                log.error("Bad bus tickets operators server response (server exception) - {}", exception);
                sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad bus tickets operators server response (local exception) - {}", exception);
                sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
            }

            @Override
            public void doTimeout() {
                log.error("Bad bus tickets operators server response (timeout exception)");
                sessionEventBus.sendNotification(new BusTicketOperatorsFailBusEvent());
            }
        }));
    }

    @Override
    protected List<AbstractMainView.UserMenuItem> getMenuBarItems() {

        List<AbstractMainView.UserMenuItem> menus = new ArrayList<>(2);

        menus.add(new AbstractMainView.UserMenuItem("Settings", new ThemeResource("images/user_menu_item_settings.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                new BusTicketsSettingsWindow(settingsAppService.getSessionSettings().getCurrentRetailerTerminal(), settingsAppService.getSessionSettings().getTerminals(), new BusTicketsSettingsWindow.SettingsSaveListener() {
                    @Override
                    public boolean onSave(SettingsSaveEvent event) {

                        BusTicketsSessionSettings settings = settingsAppService.getSessionSettings();
                        if (settings.getCurrentRetailerTerminal().equals(event.getSource().getCurrentRetailerTerminal())) {
                            ((InteractionUI) UI.getCurrent()).showNotification("Bus tickets params", "Please, select another terminal", Notification.Type.WARNING_MESSAGE);
                            return false;
                        }

                        settingsAppService.setSessionSettings(new BusTicketsSessionSettings(settings.getOperators(), settings.getTerminals(), event.getSource().getCurrentRetailerTerminal()));
                        sendBusTicketOperatorsRequest();

                        return true;
                    }
                }).show();
            }
        }, true));

        menus.add(super.getMenuBarItems().get(0));

        return menus;
    }

    @Override
    protected String getLoginTitle() {
        return "Payway Bus Tickets Dashboard";
    }

    @Override
    protected void setupWorkspaceContent() {

        updateSideBar();
        mainView.getMenuBar().setVisible(false);
        mainView.initializeUserMenu(webAppUserService.getUser().getLogin(), new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());

        //subscribe workspace on session events
        mainView.setViewActivateStateChangeListener(new AbstractMainView.ViewActivateStateChangeListener() {

            @Override
            public void onActivate(WorkspaceView workspaceView) {
                if (workspaceView != null) {
                    getSessionEventBus().addSubscriber(workspaceView);
                }
            }

            @Override
            public void onDeactivate(WorkspaceView workspaceView) {
                if (workspaceView != null) {
                    getSessionEventBus().removeSubscriber(workspaceView);
                }
            }
        });

        setContent(mainView);
    }

    private void updateSideBar() {

        mainView.clearWorkspaceView();
        mainView.getSideBarMenu().clearMenuItems();
        mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
        mainView.getSideBarMenu().select(0);
        mainView.getSideBarMenu().expand(0);
    }

    @Subscribe
    public void onLoginSuccess(LoginSuccessSessionBusEvent event) {

        try {

            List<RetailerTerminalDto> terminals = null;
            UserDto userDto = event.getUser();

            if (userDto == null) {
                throw new Exception("User sign in");
            }

            webAppUserService.setUser(new WebAppUser(userDto.getUsername(), "", event.getSessionId()));

            if (event.getExtensions() != null && event.getExtensions().size() > 0) {
                for (Object ext : event.getExtensions()) {
                    if (ext instanceof RetailerTerminalsDto) {
                        terminals = ((RetailerTerminalsDto) ext).getRetailerTerminals();
                        break;
                    }
                }
            }

            //set params to session: user, sesionId, current terminal and terminal list
            RetailerTerminalDto currentTerminal = terminals == null || terminals.isEmpty() ? null : terminals.get(0);
            settingsAppService.setSessionSettings(new BusTicketsSessionSettings(null, terminals, currentTerminal));

            setupWorkspaceContent();
            sendBusTicketOperatorsRequest();
            ((InteractionUI) UI.getCurrent()).closeProgressBar();

        } catch (Exception ex) {
            log.error("Bad user sign in - {}", ex);
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
        }
    }

    @Subscribe
    public void onBusTicketOperatorsFail(BusTicketOperatorsFailBusEvent event) {
        log.error("Bad bus tickets operators server response (exception)");
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("", "Bad bus tickets operators server response", Notification.Type.ERROR_MESSAGE);
    }

    @Subscribe
    public void onBusTicketOperatorsSuccess(BusTicketOperatorsSuccessBusEvent event) {

        //set params to session
        BusTicketsSessionSettings settings = settingsAppService.getSessionSettings();
        if (settings != null) {
            settingsAppService.setSessionSettings(new BusTicketsSessionSettings(event.getOperators(), settings.getTerminals(), settings.getCurrentRetailerTerminal()));
        }

        updateSideBar();
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }
}
