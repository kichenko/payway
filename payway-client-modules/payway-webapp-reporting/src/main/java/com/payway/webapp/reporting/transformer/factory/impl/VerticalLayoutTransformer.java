/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.payway.messaging.model.reporting.ui.VerticalLayoutStateDto;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.clara.Clara;

/**
 * VerticalLayoutTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.VerticalLayoutTransformer")
public class VerticalLayoutTransformer extends AbstractComponentTransfrormer {

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof VerticalLayoutStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of HorizontalLayout");
        }

        return (VerticalLayout) Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/VerticalLayout.xml"));
    }
}
