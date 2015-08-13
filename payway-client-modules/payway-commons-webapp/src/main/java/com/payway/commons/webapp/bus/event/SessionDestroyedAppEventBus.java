/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus.event;

/**
 * SessionDestroyedAppEventBus
 *
 * @author Sergey Kichenko
 * @created 11.08.2015
 */
public class SessionDestroyedAppEventBus extends AbstractSessionAppEventBus {

    public SessionDestroyedAppEventBus(String id) {
        super(id);
    }
}
