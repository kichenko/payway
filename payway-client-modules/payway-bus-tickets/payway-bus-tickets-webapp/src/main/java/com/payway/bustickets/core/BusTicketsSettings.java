/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.core;

import com.payway.messaging.model.common.OperatorDto;
import com.payway.messaging.model.common.RetailerTerminalDto;
import com.payway.messaging.model.user.UserDto;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BusTicketsSettings
 *
 * @author Sergey Kichenko
 * @created 15.06.15 00:00
 */
@Getter
@AllArgsConstructor
public final class BusTicketsSettings implements Serializable {

    private static final long serialVersionUID = 5356465285068553304L;

    private final UserDto user;
    private final String sessionId;
    private final List<OperatorDto> operators;
    private final List<RetailerTerminalDto> terminals;
}
