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
    REMEMBER_ME("remember_me"),
    WEB_APP_SESSION_ID("web_app_session_id"),
    SESSION_SETTINGS("session_settings");

    private final String value;

    CommonAttributes(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
