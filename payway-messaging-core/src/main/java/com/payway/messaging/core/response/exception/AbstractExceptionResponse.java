/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.response.exception;

import com.payway.messaging.core.response.ExceptionResponse;

/**
 * Абстрактный класс ошибки-ответа.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public abstract class AbstractExceptionResponse implements ExceptionResponse {

    private static final long serialVersionUID = -9208397661479988259L;

    private Integer code;
    private String message;
    private String description;

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String description() {
        return description;
    }

}
