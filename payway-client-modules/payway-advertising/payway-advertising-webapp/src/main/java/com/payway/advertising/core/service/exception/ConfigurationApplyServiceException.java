/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.exception;

/**
 * ConfigurationApplyServiceException
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public class ConfigurationApplyServiceException extends ServiceException {

    public ConfigurationApplyServiceException() {
        super();
    }

    public ConfigurationApplyServiceException(String msg) {
        super(msg);
    }

    public ConfigurationApplyServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
