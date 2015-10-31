/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.request.auth;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "password")
public final class AuthLoginPasswordCommandRequest extends AbstractAuthCommandRequest {

    private static final long serialVersionUID = -8380831312004043667L;

    private final String userName;

    private final String password;

    public AuthLoginPasswordCommandRequest(String userName, String password, String appId, String remoteAddress) {
        super(appId, remoteAddress);
        this.userName = userName;
        this.password = password;
    }
}
