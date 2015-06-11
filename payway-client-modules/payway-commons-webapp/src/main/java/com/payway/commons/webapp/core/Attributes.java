/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.core;

/**
 * Типы ключей в сессиии
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
public enum Attributes {

    USER("user_name"),
    REMEMBER_ME("remember_me"),
    CONFIG("user_configuration"),
    BUS_TICKET_OPERATORS("user_bus_ticket_operators");

    private final String value;

    Attributes(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}