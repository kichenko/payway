/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.core;

import com.payway.messaging.model.common.OperatorDto;
import com.payway.messaging.model.common.RetailerTerminalDto;
import com.payway.messaging.model.message.auth.UserDto;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * #need w/r lock
 *
 * BusTicketsSettings
 *
 * @author Sergey Kichenko
 * @created 15.06.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusTicketsSettings implements Serializable {

    private static final long serialVersionUID = 5356465285068553304L;

    private UserDto user;
    private String sessionId;
    private List<OperatorDto> operators;
    private List<RetailerTerminalDto> terminals;
}
