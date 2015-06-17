/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app;

import com.payway.bustickets.core.BusTicketAttributes;
import com.payway.bustickets.core.BusTicketsSettings;
import com.vaadin.server.VaadinSession;
import org.springframework.stereotype.Component;

/**
 * AppServiceImpl
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Component(value = "appService")
public class AppServiceImpl implements AppService {

    @Override
    public BusTicketsSettings getBusTicketsSettings() {

        BusTicketsSettings settings = null;
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            settings = (BusTicketsSettings) session.getAttribute(BusTicketAttributes.BUS_TICKET_SETTINGS.value());
        }

        return settings;
    }

    @Override
    public boolean setBusTicketsSettings(BusTicketsSettings settings) {

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(BusTicketAttributes.BUS_TICKET_SETTINGS.value(), settings);
            return true;
        }

        return false;
    }
}
