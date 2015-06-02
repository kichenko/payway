/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.web.event;

import org.springframework.context.ApplicationEvent;

/**
 * ApplicationStartClientConnectedEvent
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
public class ApplicationStartClientConnectedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -9035792640914805744L;

    public ApplicationStartClientConnectedEvent(Object source) {
        super(source);
    }
}
