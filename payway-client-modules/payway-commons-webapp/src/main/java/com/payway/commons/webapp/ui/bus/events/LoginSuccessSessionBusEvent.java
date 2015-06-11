/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.bus.events;

import com.payway.messaging.model.message.auth.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SessionBusEventLoginSuccess
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Getter
@RequiredArgsConstructor
public class LoginSuccessSessionBusEvent extends AbstractSessionBusEvent {

    final private UserDto user;

    final private String sessionId;

}
