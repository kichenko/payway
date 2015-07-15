package com.payway.vaadin.addons.ui.textfield.digit.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.vaadin.client.ui.VTextField;

public class DigitTextFieldWidget extends VTextField {

    public DigitTextFieldWidget() {

        addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {

                int keyCode = event.getNativeEvent().getKeyCode();
                if (keyCode == KeyCodes.KEY_RIGHT
                        || keyCode == KeyCodes.KEY_LEFT
                        || keyCode == KeyCodes.KEY_UP
                        || keyCode == KeyCodes.KEY_DOWN
                        || keyCode == KeyCodes.KEY_BACKSPACE
                        || keyCode == KeyCodes.KEY_ENTER
                        || keyCode == KeyCodes.KEY_TAB) {
                    return;
                }

                if (!Character.isDigit(event.getCharCode())) {
                    cancelKey();
                }
            }
        });
    }
}
