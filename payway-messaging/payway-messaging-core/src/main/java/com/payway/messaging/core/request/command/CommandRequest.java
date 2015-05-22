/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core.request.command;

import com.payway.messaging.core.request.Request;

/**
 * Абстрактный базовый класс команда-запрос, хранится в теле (Body) конверта.
 * Содержит токен пользователя (необязательно), необходимый для
 * аутентификации/авторизации действий на стороне сервера. Токен пользователя
 * выдается в ответе на запрос аутентификации/авторизации пользователя.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public abstract class CommandRequest extends Request {

    private static final long serialVersionUID = -505506877343728875L;

    private String source;
}
