/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.message.request.auth;

import com.payway.messaging.core.request.command.CommandRequest;
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
public class AuthCommandRequest<T extends com.payway.messaging.model.messaging.auth.User> extends CommandRequest {

    private static final long serialVersionUID = -8380831312004043667L;

    private T user;

    /**
     * Признак аутентификации/авторизации пользователя ч/з "запомнить меня".
     */
    private Boolean isRememberMe;

}
