/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.exception;

import lombok.NoArgsConstructor;

/**
 * SettingsStorageException
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@NoArgsConstructor
public class SettingsStorageException extends Exception {

    private static final long serialVersionUID = -7252136210640061625L;

    public SettingsStorageException(String message) {
        super(message);
    }

    public SettingsStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
