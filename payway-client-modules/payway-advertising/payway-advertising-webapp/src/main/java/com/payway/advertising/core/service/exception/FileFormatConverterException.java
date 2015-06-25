/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.exception;

/**
 * FileFormatConverterException
 *
 * @author Sergey Kichenko
 * @created 25.06.15 00:00
 */
public class FileFormatConverterException extends ServiceException {

    private static final long serialVersionUID = -5942659394778312699L;

    public FileFormatConverterException() {
        super();
    }

    public FileFormatConverterException(String msg) {
        super(msg);
    }

    public FileFormatConverterException(String msg, Throwable th) {
        super(msg, th);
    }
}
