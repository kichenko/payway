/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.core;

/**
 * Common session attributes
 *
 * @author Sergey Kichenko
 * @created 30.04.15 00:00
 */
public enum CommonAttributes {

    USER("user_name"),
    REMEMBER_ME("remember_me");

    private final String value;

    CommonAttributes(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}