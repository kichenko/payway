/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.core.service.AgentFileOwnerService;
import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.service.config.apply.ApplyConfigurationStatus;
import com.payway.advertising.core.service.config.apply.ApplyStatus;
import com.payway.advertising.core.service.config.apply.ConfigurationApplyService;
import com.payway.advertising.ui.bus.events.CloseNotificationsButtonPopupWindowsEvent;
import com.payway.advertising.ui.component.notification.NotificationsButtonPopupWindow;
import com.payway.advertising.ui.component.notification.events.apply.ApplyConfigurationNotificationEvent;
import com.payway.advertising.ui.view.core.AdvertisingMainView;
import com.payway.advertising.ui.view.core.AdvertisingSettingsWindow;
import com.payway.advertising.ui.view.workspace.content.AdvertisingContentConfigurationView;
import com.payway.commons.webapp.service.app.user.WebAppUser;
import com.payway.commons.webapp.ui.AbstractLoginUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.events.LoginSuccessSessionBusEvent;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.messaging.model.user.UserDto;
import com.payway.webapp.reporting.web.handler.ReportingContentRequestHandler;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * AdvertisingUI
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Slf4j
@SpringUI
@Theme("default")
@PreserveOnRefresh
@Widgetset("com.payway.advertising.AdvertisingWidgetSet")
public class AdvertisingUI extends AbstractLoginUI {

    private static final long serialVersionUID = -2447415985839871519L;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AdvertisingMainView mainView;

    @Autowired
    private SettingsAppService settingsAppService;

    @Getter
    @Autowired
    private ConfigurationApplyService configurationApplyService;

    @Getter
    @Autowired
    private AgentFileOwnerService agentFileOwnerService;

    @Override
    protected void setupWebApp(VaadinRequest request) {
        settingsAppService.setContextPath(request.getContextPath());

        //TODO: add reporting request handler
        VaadinSession.getCurrent().addRequestHandler(applicationContext.getBean(ReportingContentRequestHandler.class));
    }

    @Override
    protected void cleanUpOnDetach() {
        super.cleanUpOnDetach();
        sessionEventBus.removeSubscriber(mainView.getBtnNotifications());
    }

    private void closeNotificationsButtonPopupWindow() {
        for (Window window : getUI().getWindows()) {
            if (window instanceof NotificationsButtonPopupWindow) {
                window.close();
                break;
            }
        }
    }

    private void sendApplyConfigurationStatusNotification(ApplyConfigurationStatus status) {
        sessionEventBus.sendNotification(new ApplyConfigurationNotificationEvent(status.getLogin(), status.getStartTime(), status.getStatus(), status.getStatusTime(), status.getArgs()));
    }

    @Override
    protected List<SideBarMenu.MenuItem> getSideBarMenuItems() {

        List<SideBarMenu.MenuItem> items = new ArrayList<>(1);

        items.add(new SideBarMenu.MenuItem("configuration", AdvertisingContentConfigurationView.ADVERTISING_CONTENT_WORKSPACE_VIEW_ID, "Configuration", new ThemeResource("images/sidebar_configuration.png"), null, null));
        //items.add(new SideBarMenu.MenuItem("reporting", AdvertisingReportingWorkspace.ADVERTISING_REPORTING_WORKSPACE_VIEW_ID, "Reporting", new ThemeResource("images/sidebar_configuration.png"), null, null));

        return items;
    }

    private void refreshApplyConfigNotification() {

        ApplyConfigurationStatus status = configurationApplyService.getStatus();
        if (status != null && !ApplyStatus.None.equals(status.getStatus())) {
            sessionEventBus.sendNotification(new ApplyConfigurationNotificationEvent(status.getLogin(), status.getStartTime(), status.getStatus(), status.getStatusTime(), status.getArgs()));
        }
    }

    @Override
    protected List<AbstractMainView.UserMenuItem> getMenuBarItems() {

        List<AbstractMainView.UserMenuItem> menus = new ArrayList<>(2);

        menus.add(new AbstractMainView.UserMenuItem("Settings", new ThemeResource("images/user_menu_item_settings.png"), new MenuBar.Command() {
            private static final long serialVersionUID = 7160936162824727503L;

            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                new AdvertisingSettingsWindow("Settings", settingsAppService).show();
            }
        }, true));

        menus.add(super.getMenuBarItems().get(0));

        return menus;
    }

    /**
     * Refresh notifications on user sign in.
     *
     * Ex: then user signin and configuration is applying at this time - it's
     * need to notify user about this action.
     */
    private void refreshNotifications() {
        refreshApplyConfigNotification();
    }

    @Override
    protected String getLoginTitle() {
        return "Payway Advertising Desktop";
    }

    @Override
    protected void setupWorkspaceContent() {

        mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
        mainView.initializeUserMenu(webAppUserService.getUser().getLogin(), new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());
        sessionEventBus.addSubscriber(mainView.getBtnNotifications());
        mainView.getSideBarMenu().select(0);
        refreshNotifications();
        setContent(mainView);
    }

    @Subscribe
    public void processSessionBusEvent(ApplyConfigurationStatus event) {
        sendApplyConfigurationStatusNotification((ApplyConfigurationStatus) event);
    }

    @Subscribe
    public void processSessionBusEvent(CloseNotificationsButtonPopupWindowsEvent event) {
        closeNotificationsButtonPopupWindow();
    }

    @Subscribe
    public void processSessionBusEvent(LoginSuccessSessionBusEvent event) {

        try {

            UserDto userDto = event.getUser();
            if (userDto == null) {
                throw new Exception("User sign in (empty dto)");
            }

            webAppUserService.setUser(new WebAppUser(userDto.getUsername(), "", event.getSessionId()));
            setupWorkspaceContent();
            ((InteractionUI) UI.getCurrent()).closeProgressBar();

        } catch (Exception ex) {
            log.error("Bad user sign in", ex);
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
            ((InteractionUI) UI.getCurrent()).showNotification("", "Bad user sign in", Notification.Type.ERROR_MESSAGE);
        }
    }
}
