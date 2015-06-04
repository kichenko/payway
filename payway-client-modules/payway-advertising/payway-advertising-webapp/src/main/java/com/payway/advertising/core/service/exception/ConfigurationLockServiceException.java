/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.exception;

/**
 * ConfigurationLockServiceException
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public class ConfigurationLockServiceException extends ServiceException {

    private static final long serialVersionUID = 6366113541250337071L;

    public ConfigurationLockServiceException() {
        super();
    }

    public ConfigurationLockServiceException(String msg) {
        super(msg);
    }

    public ConfigurationLockServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
