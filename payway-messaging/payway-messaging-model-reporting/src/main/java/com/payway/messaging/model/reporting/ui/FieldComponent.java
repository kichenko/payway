/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.reporting.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * FieldComponent
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@Setter
public abstract class FieldComponent extends com.payway.messaging.model.reporting.ui.Component {

    private static final long serialVersionUID = 7255500110587826590L;

    protected Object value;
}
