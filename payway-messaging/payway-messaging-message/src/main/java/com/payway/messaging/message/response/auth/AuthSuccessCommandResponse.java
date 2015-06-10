/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.response.auth;

import com.payway.messaging.model.message.auth.UserDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Команда-ответ успешной аутентификации/авторизации пользователя. Объект
 * пользователь заполняется всеми возможными полями, обязательно должно быть
 * заполнено поле userToken (сгенерированный токен пользователя). Один из
 * ответов на команду AuthCommandRequest.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthSuccessCommandResponse extends AbstractAuthCommandResponse {

    private static final long serialVersionUID = -4184111918743320984L;

    final private UserDto user;

    private List<Serializable> extensions;

    public void addExtension(Serializable extension) {
        if (extensions == null) extensions = new LinkedList<>();
        extensions.add(extension);
    }

}
