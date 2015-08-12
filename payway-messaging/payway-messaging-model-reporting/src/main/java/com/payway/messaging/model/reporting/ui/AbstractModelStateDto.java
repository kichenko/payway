/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractModelStateDto
 *
 * @author Sergey Kichenko
 * @param <ID>
 * @param <VALUE>
 * @created 03.08.15 00:00
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class AbstractModelStateDto<ID, VALUE> extends AbstractDto {

    private static final long serialVersionUID = -1839884298129412870L;

    private ID id;
    private VALUE value;
}
