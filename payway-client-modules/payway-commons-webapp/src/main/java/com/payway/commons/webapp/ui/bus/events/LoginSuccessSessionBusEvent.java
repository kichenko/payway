/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.bus.events;

import com.payway.messaging.model.user.UserDto;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * LoginSuccessSessionBusEvent
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Getter
@RequiredArgsConstructor
public class LoginSuccessSessionBusEvent extends AbstractSessionBusEvent {

    private final UserDto user;

    private final String sessionId;

    private final List<Serializable> extensions;

}
