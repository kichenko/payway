/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.exception;

/**
 * ConfigurationApplyBusyException
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
public class ConfigurationApplyBusyException extends ServiceException {

    private static final long serialVersionUID = -7999330270672531639L;

    public ConfigurationApplyBusyException() {
        super();
    }

    public ConfigurationApplyBusyException(String msg) {
        super(msg);
    }

    public ConfigurationApplyBusyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
