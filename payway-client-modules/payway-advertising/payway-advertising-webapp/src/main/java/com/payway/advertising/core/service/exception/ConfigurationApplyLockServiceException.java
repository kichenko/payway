/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.exception;

/**
 * ConfigurationApplyLockServiceException
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public class ConfigurationApplyLockServiceException extends ServiceException {

    public ConfigurationApplyLockServiceException() {
        super();
    }

    public ConfigurationApplyLockServiceException(String msg) {
        super(msg);
    }

    public ConfigurationApplyLockServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
