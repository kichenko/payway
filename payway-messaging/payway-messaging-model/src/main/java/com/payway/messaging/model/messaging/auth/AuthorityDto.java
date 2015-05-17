/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.messaging.auth;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Базовая реализация роли пользователя.
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AuthorityDto implements Authority {

    private static final long serialVersionUID = -8670718557783727728L;

    private String name;

    @Override
    public String getName() {
        return name;
    }
}
