/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app;

import com.payway.commons.webapp.core.Attributes;
import com.payway.messaging.model.common.OperatorDto;
import com.payway.messaging.model.message.auth.UserDto;
import com.vaadin.server.VaadinSession;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AppServiceImpl
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Component(value = "appService")
public class AppServiceImpl implements AppService {

    @Value("#{mapBusTicketsWorkspaces}")
    private Map<String, String> mapBusTicketsWorkspaces;

    @Override
    public UserDto getUser() {

        UserDto user = null;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            user = (UserDto) session.getAttribute(Attributes.USER.value());
        }

        return user;
    }

    @Override
    public boolean setUser(UserDto user) {

        boolean isOk = false;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Attributes.USER.value(), user);
            isOk = true;
        }

        return isOk;
    }

    @Override
    public List<OperatorDto> getUserBusTicketOperators() {
        List<OperatorDto> operators = null;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            operators = (List<OperatorDto>) session.getAttribute(Attributes.BUS_TICKET_OPERATORS.value());
        }

        return operators;
    }

    @Override
    public boolean setUserBusTicketOperators(List<OperatorDto> operators) {
        boolean isOk = false;

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(Attributes.BUS_TICKET_OPERATORS.value(), operators);
            isOk = true;
        }

        return isOk;
    }

    @Override
    public String getBusTicketOperatorWorkspaceViewName(String operatorId) {
        return mapBusTicketsWorkspaces.get(operatorId);
    }
}
