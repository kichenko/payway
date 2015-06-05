/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.web.event;

import org.springframework.context.ApplicationEvent;

/**
 * ApplicationStartClientConnectedEvent
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
public class ApplicationStartClientConnectedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -914095531118131076L;

    public ApplicationStartClientConnectedEvent(Object source) {
        super(source);
    }
}
