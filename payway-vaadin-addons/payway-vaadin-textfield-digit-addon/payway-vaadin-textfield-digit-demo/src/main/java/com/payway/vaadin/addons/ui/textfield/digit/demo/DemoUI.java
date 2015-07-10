package com.payway.vaadin.addons.ui.textfield.digit.demo;

import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import javax.servlet.annotation.WebServlet;

@Theme("demo")
@Title("DigitTextField Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "com.payway.vaadin.addons.ui.textfield.digit.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        // Initialize our new UI component
        final DigitTextField component = new DigitTextField();
        component.setImmediate(true);
        component.setStyleName("numerical");
        component.setConverter(Integer.class);
        component.setNullRepresentation("0");
        component.setValue("15");
        component.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show("Text is:" + (String)event.getProperty().getValue(), Notification.Type.TRAY_NOTIFICATION);
            }
        });

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(component);
        layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
        setContent(layout);
    }
}
