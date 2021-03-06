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
public class ConfigurationApplyCancelException extends ServiceException {

    private static final long serialVersionUID = -768482583826251441L;

    public ConfigurationApplyCancelException() {
        super();
    }

    public ConfigurationApplyCancelException(String msg) {
        super(msg);
    }

    public ConfigurationApplyCancelException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
