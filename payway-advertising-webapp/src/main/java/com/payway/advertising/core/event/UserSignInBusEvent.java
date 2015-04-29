/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UserSignInBusEvent
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class UserSignInBusEvent implements AdminBusEvent {

    private String userName;
    private String passowrd;
    private boolean isRememberMe;
}
