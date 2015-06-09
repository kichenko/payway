/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app;

import com.payway.messaging.model.common.OperatorDto;
import com.payway.messaging.model.message.auth.UserDto;
import java.util.List;

/**
 * AppService
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
public interface AppService {

    public UserDto getUser();

    public boolean setUser(UserDto user);

    public List<OperatorDto> getUserBusTicketOperators();

    public boolean setUserBusTicketOperators(List<OperatorDto> operators);

    public String getBusTicketOperatorWorkspaceViewName(String operatorId);
}
