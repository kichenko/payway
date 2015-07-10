package com.payway.vaadin.addons.ui.textfield.digit.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;
import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(DigitTextField.class)
public class DigitTextFieldConnector extends TextFieldConnector {

    private static final long serialVersionUID = 8912614383151224539L;

    public DigitTextFieldConnector() {

        getWidget().addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (!Character.isDigit(event.getCharCode())) {
                    getWidget().cancelKey();
                }
            }
        });
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(DigitTextFieldWidget.class);
    }

    @Override
    public DigitTextFieldWidget getWidget() {
        return (DigitTextFieldWidget) super.getWidget();
    }
}
