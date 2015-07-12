package com.payway.vaadin.addons.ui.textfield.digit.client;

import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = DigitTextField.class)
public class DigitTextFieldConnector extends TextFieldConnector {

    private static final long serialVersionUID = 8912614383151224539L;

    public DigitTextFieldConnector() {
        //
    }

    @Override
    public DigitTextFieldWidget getWidget() {
        return (DigitTextFieldWidget) super.getWidget();
    }
}
