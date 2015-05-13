/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

/**
 * ServiceException
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public class ServiceException extends Exception {

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
