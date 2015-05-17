/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.core;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Тело конверта. Содержит данные, например запрос на аутентификацию/авторизацию
 * пользователя или ответ на этот запрос.
 *
 * @author Sergey Kichenko
 * @param <T>
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Body<T extends Message> implements Serializable {

    private static final long serialVersionUID = -1956721521675807295L;

    /**
     * Данные сообщения, например запрос/ответ аутентификации/авторизации
     * пользователя.
     */
    private T message;
}
