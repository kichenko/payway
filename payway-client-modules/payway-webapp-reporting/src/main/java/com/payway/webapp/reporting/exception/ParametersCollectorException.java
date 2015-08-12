/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.reporting.exception;

import lombok.NoArgsConstructor;

/**
 * ParametersCollectorException
 *
 * @author Sergey Kichenko
 * @created 06.08.2015
 */
@NoArgsConstructor
public class ParametersCollectorException extends Exception {

    private static final long serialVersionUID = 5504101122135439893L;

    public ParametersCollectorException(String message) {
        super(message);
    }

    public ParametersCollectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
