/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.ui;

import com.payway.spring.beans.VaadinSampleSpringBean;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * Vaadin sample UI
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@SpringUI
@Theme("valo")
public class VaadinSampleUI extends UI {

    @Autowired
    private VaadinSampleSpringBean bean;

    @UiField("layout")
    VerticalLayout layout;

    @UiField("button")
    private Button button;

    @Override
    protected void init(VaadinRequest request) {
        setContent(Clara.create("VaadinSampleUI.xml", this));
    }

    @UiHandler("button")
    public void removeSelectedContact(Button.ClickEvent event) {
        layout.addComponent(new Label(bean.getCaption()));
    }
}
