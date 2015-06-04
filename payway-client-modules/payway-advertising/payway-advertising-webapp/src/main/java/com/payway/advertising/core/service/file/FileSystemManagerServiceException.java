/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.file;

import com.payway.advertising.core.service.exception.ServiceException;

/**
 * FileSystemManagerServiceException
 *
 * @author Sergey Kichenko
 * @created 08.10.15 00:00
 */
public class FileSystemManagerServiceException extends ServiceException {
    
    private static final long serialVersionUID = -5415950541787253695L;

    public FileSystemManagerServiceException() {
        super();
    }

    public FileSystemManagerServiceException(String msg) {
        super(msg);
    }

    public FileSystemManagerServiceException(String msg, Throwable th) {
        super(msg, th);
    }
}
