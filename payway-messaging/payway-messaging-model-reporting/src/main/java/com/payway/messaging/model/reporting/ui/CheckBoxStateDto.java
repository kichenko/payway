/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

/**
 * CheckBoxStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
public class CheckBoxStateDto extends FieldComponentStateDto {
    
    private static final long serialVersionUID = 6648488666509936049L;
    
    public CheckBoxStateDto(String name, String caption, boolean value) {
        setName(name);
        setCaption(caption);
        setValue(value);
    }
}
