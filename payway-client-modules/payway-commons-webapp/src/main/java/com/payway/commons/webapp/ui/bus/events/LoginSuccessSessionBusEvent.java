/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.bus.events;

import com.payway.messaging.model.message.auth.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * SessionBusEventLoginSuccess
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessSessionBusEvent extends AbstractSessionBusEvent {

    private UserDto user;
}
