/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * AppBusEventImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@ToString
@AllArgsConstructor
public final class AppBusEventImpl implements AppBusEvent {

    private final Object data;

    public AppBusEventImpl() {
        data = null;
    }

    @Override
    public Object getData() {
        return data;
    }
}
