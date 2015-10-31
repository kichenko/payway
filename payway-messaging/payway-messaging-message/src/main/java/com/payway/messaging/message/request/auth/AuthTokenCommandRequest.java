/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.request.auth;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * AuthTokenCommandRequest
 *
 * @author Sergey Kichenko
 * @created 17.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class AuthTokenCommandRequest extends AbstractAuthCommandRequest {

    private static final long serialVersionUID = -3344657923432959429L;

    private final String token;

    public AuthTokenCommandRequest(String token, String appId, String remoteAddress) {
        super(appId, remoteAddress);
        this.token = token;
    }
}
