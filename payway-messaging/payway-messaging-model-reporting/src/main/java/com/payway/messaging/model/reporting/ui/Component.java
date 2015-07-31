/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Component
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@Setter
public abstract class Component implements Serializable {

    private static final long serialVersionUID = 6845601714770261635L;

    protected String name;
    protected String caption;
    protected boolean enabled;
    protected boolean visible;
}
