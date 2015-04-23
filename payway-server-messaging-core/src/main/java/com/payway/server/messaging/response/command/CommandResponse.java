/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging.response.command;

import com.payway.server.messaging.response.Response;

/**
 * Абстрактный базовый класс команда-ответ, хранится в теле (Body) конверта.
 * Содержит токен пользователя, который запросил команду (необязательно). Токен
 * пользователя выдается в ответе на команду-запрос аутентификации/авторизации
 * пользователя.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public abstract class CommandResponse extends Response {

    private String userToken;
}
