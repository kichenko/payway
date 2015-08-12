/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.payway.messaging.model.reporting.ui.HorizontalLayoutStateDto;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.teemu.clara.Clara;

/**
 * HorizontalLayoutTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.HorizontalLayoutTransformer")
public class HorizontalLayoutTransformer extends AbstractComponentTransfrormer {

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof HorizontalLayoutStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of HorizontalLayout");
        }

        return (HorizontalLayout) Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/HorizontalLayout.xml"));
    }
}
