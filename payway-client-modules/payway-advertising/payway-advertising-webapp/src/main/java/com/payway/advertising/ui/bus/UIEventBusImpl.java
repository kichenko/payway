/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.bus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * UIEventBusImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
@UIScope
@Component(value = "uiEventBus")
public class UIEventBusImpl implements UIEventBus, SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    @Override
    public void addSubscriber(Object subscriber) {
        eventBus.register(subscriber);
    }

    @Override
    public void removeSubscriber(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    @Override
    public void sendNotification(Object event) {
        eventBus.post(event);
    }

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error("Error in app event bus", exception);
    }
}
