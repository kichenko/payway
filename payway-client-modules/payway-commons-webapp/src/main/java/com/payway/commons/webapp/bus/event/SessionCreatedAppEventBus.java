/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus.event;

/**
 * SessionCreatedAppEventBus
 *
 * @author Sergey Kichenko
 * @created 11.08.2015
 */
public class SessionCreatedAppEventBus extends AbstractSessionAppEventBus {

    private static final long serialVersionUID = 2777430474251644406L;

    public SessionCreatedAppEventBus(String id) {
        super(id);
    }
}
