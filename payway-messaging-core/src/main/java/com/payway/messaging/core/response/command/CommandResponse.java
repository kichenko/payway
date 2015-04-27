/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.core.response.command;

import com.payway.messaging.core.response.SuccessResponse;

/**
 * Абстрактный базовый класс команда-ответ, хранится в теле (Body) конверта.
 * Содержит токен пользователя, который запросил команду (необязательно). Токен
 * пользователя выдается в ответе на команду-запрос аутентификации/авторизации
 * пользователя.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public abstract class CommandResponse implements SuccessResponse {

    private String userToken;
}
