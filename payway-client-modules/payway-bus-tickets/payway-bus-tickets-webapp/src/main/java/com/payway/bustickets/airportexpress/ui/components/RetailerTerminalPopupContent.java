/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.payway.bustickets.airportexpress.ui.components.containers.RetailerTerminalDtoBeanContainer;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupView;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * RetailerTerminalPopupViewContent
 *
 * @author Sergey Kichenko
 * @created 11.06.15 00:00
 */
public class RetailerTerminalPopupContent implements PopupView.Content {

    private static final long serialVersionUID = 1055678631383122709L;

    private String caption = "select";
    private Component content;

    @UiField
    private ComboBox cbRetailerTerminal;

    @UiField
    private Button btnSave;

    @UiField
    private Button btnCancel;

    public RetailerTerminalPopupContent(String caption) {
        this.caption = caption;
        init();
    }

    private void init() {

        content = Clara.create("RetailerTerminalPopupContentView.xml", this);
        cbRetailerTerminal.setContainerDataSource(new RetailerTerminalDtoBeanContainer());
        
        cbRetailerTerminal.addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -382717228031608542L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                //
            }
        });

        btnSave.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                //
            }
        });

        btnCancel.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                //
            }
        });
    }

    @Override
    public final Component getPopupComponent() {
        return content;
    }

    @Override
    public final String getMinimizedValueAsHTML() {
        return caption;
    }
}
