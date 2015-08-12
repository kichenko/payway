/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.payway.messaging.model.reporting.ui.IntervalStateDto;
import com.payway.webapp.reporting.components.Interval;
import com.vaadin.ui.Component;

/**
 * IntervalTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.IntervalTransformer")
public class IntervalTransformer extends AbstractComponentTransfrormer {

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof IntervalStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of Interval");
        }

        IntervalStateDto interval = (IntervalStateDto) cmp;

        return new Interval(interval.getName(), interval.getFrom(), interval.getTo());
    }
}
