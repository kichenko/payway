/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * DateTimePickerStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@Setter
public class DateTimePickerStateDto extends FieldComponentStateDto {

    private static final long serialVersionUID = 5080837124367752290L;

    public enum DateFormatType {

        Date,
        Time,
        DateTime,
        Timestamp
    }

    protected DateFormatType kind;

    public DateTimePickerStateDto(String name, String caption, DateFormatType kind, Date value) {
        setName(name);
        setCaption(caption);
        setValue(value);
        setKind(kind);
    }
}
