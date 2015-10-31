/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.service.app.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * WebAppUser
 *
 * @author Sergey Kichenko
 * @created 17.07.15 00:00
 */
@Getter
@AllArgsConstructor
public final class WebAppUser {

    private final String login;
    private final String password;
    private final String sessionId;
}
