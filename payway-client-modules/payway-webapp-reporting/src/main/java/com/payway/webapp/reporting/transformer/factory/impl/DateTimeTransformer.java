/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.transformer.factory.impl;

import com.payway.messaging.model.reporting.ui.ComponentStateDto;
import com.payway.messaging.model.reporting.ui.DateTimePickerStateDto;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.PopupDateField;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.teemu.clara.Clara;

/**
 * DateTimeTransformer
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@org.springframework.stereotype.Component(value = "app.reporting.transformer.DateTimeTransformer")
public class DateTimeTransformer extends AbstractComponentTransfrormer {

    @Value("yyyy-MM-dd")
    private String formatDate;

    @Value("HH:mm")
    private String formatTime;

    @Value("yyyy-MM-dd HH:mm")
    private String formatDateTime;

    @Value("yyyy-MM-dd HH:mm:ss")
    private String formatTimeStamp;

    @Override
    public Component transform(ComponentStateDto cmp) throws Exception {

        if (!(cmp instanceof DateTimePickerStateDto)) {
            throw new IllegalArgumentException("Argument is not instance of DateTimePicker");
        }

        PopupDateField popupDateField = (PopupDateField) Clara.create(getClass().getResourceAsStream("/com/payway/webapp/reporting/components/PopupDateField.xml"));

        popupDateField.setId(cmp.getName());
        popupDateField.setCaption(cmp.getCaption());
        popupDateField.setValue((Date) ((DateTimePickerStateDto) cmp).getValue());

        configure(popupDateField, ((DateTimePickerStateDto) cmp).getKind());

        return popupDateField;
    }

    private void configure(PopupDateField popupDateField, DateTimePickerStateDto.DateFormatType kind) {

        switch (kind) {
            case Date: {
                popupDateField.setDateFormat(formatDate);
                popupDateField.setResolution(Resolution.DAY);
            }
            break;

            case DateTime: {
                popupDateField.setDateFormat(formatDateTime);
                popupDateField.setResolution(Resolution.MINUTE);
            }
            break;

            case Time: {
                popupDateField.setDateFormat(formatTime);
                popupDateField.setResolution(Resolution.MINUTE);
            }
            break;

            case Timestamp: {
                popupDateField.setDateFormat(formatTimeStamp);
                popupDateField.setResolution(Resolution.SECOND);
            }
            break;

            default:
        }
    }
}
