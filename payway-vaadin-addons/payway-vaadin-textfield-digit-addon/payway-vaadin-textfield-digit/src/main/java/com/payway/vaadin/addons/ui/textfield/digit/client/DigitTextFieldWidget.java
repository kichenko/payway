package com.payway.vaadin.addons.ui.textfield.digit.client;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.vaadin.client.ui.VTextField;

public class DigitTextFieldWidget extends VTextField {

    public DigitTextFieldWidget() {

        addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                if (!Character.isDigit(event.getCharCode())) {
                    cancelKey();
                }
            }
        });
    }
}
