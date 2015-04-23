/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging.request.auth;

import com.payway.server.messaging.auth.core.User;
import com.payway.server.messaging.request.command.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Команда-запрос на аутентификацию/авторизацию пользователя. Токен пользователя
 * игнорируется. Если флаг "isRememberMe == true" объект пользователь
 * заполняется только полем userToken, иначе полями username/password.
 *
 * @author Sergey Kichenko
 * @param <T>
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthCommandRequest<T extends User> extends CommandRequest {

    private static final long serialVersionUID = -8380831312004043667L;

    /**
     * Признак аутентификации/авторизации пользователя ч/з "запомнить меня".
     */
    private Boolean isRememberMe;

    private T user;

}
