/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core.response.exception;

import com.payway.messaging.core.response.ExceptionResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.ToString;

/**
 * Абстрактный класс ошибки-ответа.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@ToString
public abstract class AbstractExceptionResponse implements ExceptionResponse {

    private static final long serialVersionUID = -9208397661479988259L;

    private String code;

    private String message;

    private String description;

    public AbstractExceptionResponse(Throwable t) {
        this(t.getClass().getName(), t.getMessage(), getStackTrace(t));
    }

    public AbstractExceptionResponse(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDescription() {
        return description;
    }

    private static String getStackTrace(Throwable t) {
        StringWriter stackTrace = new StringWriter();
        t.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }

}
