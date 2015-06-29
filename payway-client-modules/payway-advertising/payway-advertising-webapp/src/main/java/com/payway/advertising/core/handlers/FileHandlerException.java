/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

/**
 * FileHandlerException
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public class FileHandlerException extends Exception {

    private static final long serialVersionUID = -262834845225805706L;

    public FileHandlerException() {
        super();
    }

    public FileHandlerException(String msg) {
        super(msg);
    }

    public FileHandlerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
