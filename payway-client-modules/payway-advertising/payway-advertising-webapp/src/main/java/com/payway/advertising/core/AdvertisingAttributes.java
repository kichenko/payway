/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core;

/**
 * AdvertisingAttributes
 *
 * @author Sergey Kichenko
 * @created 15.06.15 00:00
 */
public enum AdvertisingAttributes {

    CONFIG("user_configuration");

    private final String value;

    AdvertisingAttributes(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
