/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.web.event;

import org.springframework.context.ApplicationEvent;

/**
 * ApplicationStartEvent
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
public class ApplicationStartEvent extends ApplicationEvent {

    private static final long serialVersionUID = 6522902249554515731L;

    public ApplicationStartEvent(Object source) {
        super(source);
    }
}
