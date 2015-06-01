/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.web;

import org.springframework.context.ApplicationEvent;

/**
 * ApplicationOnStartClientConnectedEvent
 *
 * @author Sergey Kichenko
 * @created 01.06.15 00:00
 */
public class ApplicationOnStartClientConnectedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -9035792640914805744L;

    public ApplicationOnStartClientConnectedEvent(Object source) {
        super(source);
    }
}
