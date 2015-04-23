/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.server.messaging.auth.core;

import java.io.Serializable;
import java.util.Collection;

/**
 * Интерфейс представляющий базовую инФормацию о пользователе.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public interface User extends Serializable {

    String username();

    String password();

    /**
     * Токен пользователя - строка однозначно идентифицирующая пользователя,
     * например md5(userName+password+salt). Используется также для функционала
     * "Remember Me".
     *
     * @return токен пользователя
     */
    String userToken();

    Boolean isEnabled();

    Collection<Authority> authorities();

}
