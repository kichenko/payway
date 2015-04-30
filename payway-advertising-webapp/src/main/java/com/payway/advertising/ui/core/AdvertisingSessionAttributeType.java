/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.core;

/**
 * Типы ключей в сессиии
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
public enum AdvertisingSessionAttributeType {

    USER("user");

    private final String value;

    AdvertisingSessionAttributeType(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
