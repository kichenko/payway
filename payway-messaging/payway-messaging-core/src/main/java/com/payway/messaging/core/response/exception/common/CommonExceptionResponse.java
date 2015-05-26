/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core.response.exception.common;

import com.payway.messaging.core.response.exception.AbstractExceptionResponse;
import lombok.ToString;

/**
 * Ответ-ошибка (любая ошибка). Возможный ответ на любой ЗАПРОС.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@ToString(callSuper = true)
public class CommonExceptionResponse extends AbstractExceptionResponse {

    private static final long serialVersionUID = 8081837348268102455L;

    public CommonExceptionResponse(Throwable t) {
        super(t);
    }

    public CommonExceptionResponse(String message) {
        this(null, message, null);
    }

    public CommonExceptionResponse(String code, String message) {
        this(code, message, null);
    }

    public CommonExceptionResponse(String code, String message, String description) {
        super(code, message, description);
    }

}
