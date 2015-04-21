/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.admin.ui;

import com.payway.ui.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

/**
 * UI viewport of admin webapp
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@SpringUI
@Theme("default")
@Widgetset("com.payway.admin.AdminWidgetSet")
public final class AdminUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        //setContent(new LoginView());
        setContent(new MainView());
    }
}
