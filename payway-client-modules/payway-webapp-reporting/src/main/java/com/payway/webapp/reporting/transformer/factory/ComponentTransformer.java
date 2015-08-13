/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory;

import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.vaadin.ui.Component;

/**
 * ComponentTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
public interface ComponentTransformer {

    Component transform(ComponentStateDto cmp) throws Exception;
}
