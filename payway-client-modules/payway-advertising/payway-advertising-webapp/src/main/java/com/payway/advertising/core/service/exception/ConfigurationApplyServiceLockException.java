/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.exception;

/**
 * ConfigurationApplyServiceLockException
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public class ConfigurationApplyServiceLockException extends ServiceException {

    public ConfigurationApplyServiceLockException() {
        super();
    }

    public ConfigurationApplyServiceLockException(String msg) {
        super(msg);
    }

    public ConfigurationApplyServiceLockException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
