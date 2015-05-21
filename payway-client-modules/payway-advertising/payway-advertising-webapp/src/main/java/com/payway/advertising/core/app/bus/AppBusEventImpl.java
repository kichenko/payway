/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.app.bus;

import lombok.AllArgsConstructor;

/**
 * AppBusEventImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
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
