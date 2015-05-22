package com.payway.advertising.web;

import org.springframework.context.ApplicationEvent;

/**
 * Created by mike on 20/05/15.
 */
public class ApplicationStartEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public ApplicationStartEvent(Object source) {
        super(source);
    }

}
