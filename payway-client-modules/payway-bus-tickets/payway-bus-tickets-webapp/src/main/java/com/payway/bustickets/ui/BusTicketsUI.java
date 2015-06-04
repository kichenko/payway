/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui;

import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.LoginView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
    private AbstractMainView mainView;

    @Autowired
    private LoginView loginView;

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
        Collection<SideBarMenu.MenuItem> items = new ArrayList<>(5);
        items.add(new SideBarMenu.MenuItem("bus-tickets", "Bus tickets", new ThemeResource("images/sidebar_bus_tickets.png")));
        return items;
    }

    private void updateContent() {

        //loginView.setTitle("Payway BusTickets Desktop");
        //loginView.initialize();
        //setContent(loginView);
        mainView.initializeSideBarMenu(getSideBarMenuItems(), null);
        mainView.initializeUserMenu("hello", new ThemeResource("images/user_menu_bar_main.png"), getMenuBarItems());
        mainView.getSideBarMenu().select(0);
        setContent(mainView);
    }
}
