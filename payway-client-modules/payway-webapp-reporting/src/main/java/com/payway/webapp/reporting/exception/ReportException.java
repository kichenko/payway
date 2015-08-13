/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.exception;

import lombok.NoArgsConstructor;

/**
 * ReportException
 *
 * @author Sergey Kichenko
 * @created 04.08.15 00:00
 */
@NoArgsConstructor
public class ReportException extends Exception {

    private static final long serialVersionUID = 5504101122135439893L;

    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
