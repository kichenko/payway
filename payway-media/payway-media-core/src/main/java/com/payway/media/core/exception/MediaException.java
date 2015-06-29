/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.exception;

/**
 * Codec
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public class MediaException extends Exception {

    private static final long serialVersionUID = -7327992351350678361L;

    public MediaException(String message) {
        super(message);
    }

    public MediaException(String message, Throwable cause) {
        super(message, cause);
    }
}
