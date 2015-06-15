/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.core;

/**
 * BusTicketAttributes
 *
 * @author Sergey Kichenko
 * @created 15.06.15 00:00
 */
public enum BusTicketAttributes {

    BUS_TICKET_SETTINGS("user_bus_ticket_operators");

    private final String value;

    BusTicketAttributes(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
