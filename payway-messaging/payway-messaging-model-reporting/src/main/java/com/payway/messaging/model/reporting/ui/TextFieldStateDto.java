/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

/**
 * TextFieldStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public class TextFieldStateDto extends FieldComponentStateDto {

    private static final long serialVersionUID = -54910124973261399L;

    public TextFieldStateDto(String name, String caption, Object value) {
        setName(name);
        setCaption(caption);
        setValue(value);
    }
}
