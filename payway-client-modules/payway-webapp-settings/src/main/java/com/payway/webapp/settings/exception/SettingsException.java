/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.exception;

import lombok.NoArgsConstructor;

/**
 * SettingsException
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@NoArgsConstructor
public class SettingsException extends Exception {

    private static final long serialVersionUID = 8661362873880823030L;

    public SettingsException(String message) {
        super(message);
    }

    public SettingsException(String message, Throwable cause) {
        super(message, cause);
    }

}
