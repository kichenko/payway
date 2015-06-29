
/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

/**
 * FileProcessorException
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public class FileProcessorException extends Exception {

    private static final long serialVersionUID = -3930211452647262700L;

    public FileProcessorException() {
        super();
    }

    public FileProcessorException(String msg) {
        super(msg);
    }

    public FileProcessorException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
