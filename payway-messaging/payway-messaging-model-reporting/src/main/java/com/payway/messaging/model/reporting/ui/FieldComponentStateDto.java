/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * FieldComponentStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@Setter
public abstract class FieldComponentStateDto extends ComponentStateDto {

    private static final long serialVersionUID = 7255500110587826590L;

    protected Object value;
}
