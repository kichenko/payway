/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

/**
 * SimpleComponentModelStateDto
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
public class SimpleComponentModelStateDto extends AbstractModelStateDto<Object, String> {

    private static final long serialVersionUID = 4262100785840424899L;

    public SimpleComponentModelStateDto(Object id, String value) {
        super(id, value);
    }

}
