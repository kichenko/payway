/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.response.exception.command;

import com.payway.messaging.core.response.command.CommandResponse;
import com.payway.messaging.core.response.exception.ExceptionResponse;

/**
 * Абстрактный класс команды-ответа ошибки. Содержит токен пользователя, который
 * запросил команду (необязательно). Возможный ответ на любую КОМАНДУ-ЗАПРОС.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public class AbstractCommandResponseException extends CommandResponse implements ExceptionResponse {

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
