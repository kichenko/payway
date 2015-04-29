/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UI viewport of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@SpringUI
@Theme("default")
@Widgetset("com.payway.advertising.AdvertisingWidgetSet")
public final class AdvertisingUI extends UI {

    @Autowired
    public SpringViewProvider viewProvider;

    private boolean isAuth = false;

    public AdvertisingUI() {
        int k = 0;
    }

    @PostConstruct
    public void post() {
        int k = 0;
    }

    @Override
    protected void init(VaadinRequest request) {

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();
        container.setMargin(false);
        container.setSpacing(false);
        setContent(container);

        updateContent(container);
    }

    private void updateContent(ComponentContainer container) {
        if (isAuth) {
            //
        } else {
            Navigator navigator = new Navigator(UI.getCurrent(), container);
            navigator.addProvider(viewProvider);
            navigator.navigateTo("login");
        }
    }
}
