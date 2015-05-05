/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.model.messaging.auth;

import java.io.Serializable;

/**
 * Интерфейс представляющий базовую инФормацию о роли.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
public interface Authority extends Serializable {

    String name();
}
