/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer;

import com.payway.webapp.reporting.exception.ReportException;
import com.vaadin.ui.Component;

/**
 * ReportUITransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
public interface ReportUITransformer {

    Component transform(com.payway.messaging.model.reporting.ui.ComponentStateDto content) throws ReportException;
}
