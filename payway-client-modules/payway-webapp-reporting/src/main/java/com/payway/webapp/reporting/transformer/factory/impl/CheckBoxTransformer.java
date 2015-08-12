/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.payway.messaging.model.reporting.ui.CheckBoxStateDto;
import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import org.vaadin.teemu.clara.Clara;

/**
 * CheckBoxTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.CheckBoxTransformer")
public class CheckBoxTransformer extends AbstractComponentTransfrormer {

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof CheckBoxStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of CheckBox");
        }

        CheckBox checkbox = (CheckBox) Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/CheckBox.xml"));

        checkbox.setId(cmp.getName());
        checkbox.setCaption(cmp.getCaption());
        checkbox.setValue((Boolean) ((CheckBoxStateDto) cmp).getValue());

        return checkbox;
    }
}
