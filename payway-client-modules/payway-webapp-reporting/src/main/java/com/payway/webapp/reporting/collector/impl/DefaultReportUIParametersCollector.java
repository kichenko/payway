/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.collector.impl;

import com.payway.messaging.model.reporting.ReportParameterDto;
import com.payway.webapp.reporting.collector.ParametersCollector;
import com.payway.webapp.reporting.components.Container;
import com.payway.webapp.reporting.components.EntityBox;
import com.payway.webapp.reporting.components.ListBox;
import com.payway.webapp.reporting.components.model.SimpleSelectionModel;
import com.payway.webapp.reporting.exception.ParametersCollectorException;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * DefaultReportUIParametersCollector
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
@Slf4j
@org.springframework.stereotype.Component(value = "app.reporting.collector.DefaultReportUIParametersCollector")
public class DefaultReportUIParametersCollector implements ParametersCollector {

    @Override
    public List<ReportParameterDto> collect(ComponentContainer layout) throws ParametersCollectorException {

        List<ReportParameterDto> params = new LinkedList<>();

        try {
            walk(layout, params);
        } catch (Exception ex) {
            throw new ParametersCollectorException(ex.getMessage(), ex);
        }

        return params;
    }

    private void walk(Component cmp, List<ReportParameterDto> params) throws Exception {

        if (cmp instanceof Layout && !(cmp instanceof ListBox || cmp instanceof EntityBox)) {
            Iterator<Component> iterator = ((ComponentContainer) cmp).iterator();
            while (iterator.hasNext()) {
                walk(iterator.next(), params);
            }
        } else {

            switch (cmp.getClass().getSimpleName()) {

                case "ListBox":
                case "EntityBox": {
                    convertContainerModel2ReportParameters(cmp.getId(), (Container) cmp, params);
                }
                break;

                case "CheckBox":
                case "TextField":
                case "PopupDateField": {
                    params.add(new ReportParameterDto(cmp.getId(), ((AbstractField) cmp).getValue()));
                }
                break;

                default: {
                    String msg = String.format("Not supported parsing report parameter of component id= [%s], class=[%s]", cmp.getId(), cmp.getClass().getName());
                    log.debug(msg);
                }
            }
        }
    }

    private void convertContainerModel2ReportParameters(String componentId, Container container, List<ReportParameterDto> params) {

        List<SimpleSelectionModel> selected = container.getSelected();

        if (selected.isEmpty()) {
            return;
        }

        if (selected.size() == 1) {
            params.add(new ReportParameterDto(componentId, selected.get(0).getId()));
        } else {

            List<String> array = new ArrayList<>(selected.size());
            for (SimpleSelectionModel m : selected) {
                array.add(m.getId().toString());
            }

            params.add(new ReportParameterDto(componentId, array.toArray(new String[array.size()])));
        }
    }
}
