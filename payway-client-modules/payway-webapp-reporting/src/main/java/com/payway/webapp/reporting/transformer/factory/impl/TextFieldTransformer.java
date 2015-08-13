/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.payway.messaging.model.reporting.ui.TextFieldStateDto;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.vaadin.teemu.clara.Clara;

/**
 * TextFieldTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.TextFieldTransformer")
public class TextFieldTransformer extends AbstractComponentTransfrormer {

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof com.payway.messaging.model.reporting.ui.TextFieldStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of TextField");
        }

        TextField textField = (TextField) Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/TextField.xml"));

        textField.setId(cmp.getName());
        textField.setCaption(cmp.getCaption());
        textField.setValue((String) ((TextFieldStateDto) cmp).getValue());

        return textField;
    }
}
