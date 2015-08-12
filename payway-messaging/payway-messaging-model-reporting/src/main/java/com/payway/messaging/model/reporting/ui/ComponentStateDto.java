/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import com.payway.messaging.model.AbstractDto;
import lombok.Getter;
import lombok.Setter;

/**
 * ComponentStateDto
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@Setter
public abstract class ComponentStateDto extends AbstractDto {

    private static final long serialVersionUID = 6845601714770261635L;

    protected String name;
    protected String caption;
    protected boolean enabled;
    protected boolean visible;
}
