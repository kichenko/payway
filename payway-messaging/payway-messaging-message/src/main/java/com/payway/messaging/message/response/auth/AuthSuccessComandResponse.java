/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.messaging.message.response.auth;

import com.payway.messaging.model.messaging.auth.UserDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Команда-ответ успешной аутентификации/авторизации пользователя. Объект
 * пользователь заполняется всеми возможными полями, обязательно должно быть
 * заполнено поле userToken (сгенерированный токен пользователя). Один из
 * ответов на команду AuthCommandRequest.
 *
 * @author Sergey Kichenko
 * @param <T>
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthSuccessComandResponse<T extends UserDto> extends AbstractAuthCommandResponse {

    private static final long serialVersionUID = -4184111918743320984L;

    private T user;
}
