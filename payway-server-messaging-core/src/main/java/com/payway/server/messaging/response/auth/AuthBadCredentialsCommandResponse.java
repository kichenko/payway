/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging.response.auth;

import com.payway.server.messaging.response.exception.command.CommandResponseException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Команда-ответ неверный логин/пароль пользователя. Токен пользователя
 * игнорируется. Один из ответов на команду AuthCommandRequest.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthBadCredentialsCommandResponse extends CommandResponseException {

    private static final long serialVersionUID = 1031314572819220266L;

}
