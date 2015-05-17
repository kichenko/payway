/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core.response.exception.command;

import com.payway.messaging.core.response.ExceptionResponse;
import lombok.Setter;

/**
 * Абстрактный класс команды-ответа ошибки. Содержит токен пользователя, который
 * запросил команду (необязательно). Возможный ответ на любую КОМАНДУ-ЗАПРОС.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Setter
public class AbstractCommandResponseException implements ExceptionResponse {

    private static final long serialVersionUID = 4024035156593304870L;

    private Integer code;
    private String message;
    private String description;

    @Override
    public Integer getCode() {
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

}
