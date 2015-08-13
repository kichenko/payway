/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.collector;

import com.payway.messaging.model.reporting.ReportParameterDto;
import com.payway.webapp.reporting.exception.ParametersCollectorException;
import com.vaadin.ui.ComponentContainer;
import java.util.List;

/**
 * ParametersCollector
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
public interface ParametersCollector {

    List<ReportParameterDto> collect(ComponentContainer layout) throws ParametersCollectorException;
}
