/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import com.payway.messaging.model.AbstractDto;

/**
 * AbstractModelStateDto
 *
 * @author Sergey Kichenko
 * @param <ID>
 * @param <VALUE>
 * @created 03.08.15 00:00
 */
public abstract class AbstractModelStateDto<ID, VALUE> extends AbstractDto {

    private static final long serialVersionUID = -1839884298129412870L;

    private ID id;

    private VALUE value;

    public AbstractModelStateDto(ID id, VALUE value) {
        this.id = id;
        this.value = value;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public VALUE getValue() {
        return value;
    }

    public void setValue(VALUE value) {
        this.value = value;
    }

}
