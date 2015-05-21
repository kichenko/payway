/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.bus;

import com.google.common.eventbus.EventBus;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

/**
 * UIEventBusImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@UIScope
@Component(value = "uiEventBus")
public class UIEventBusImpl implements UIEventBus {

    private final EventBus eventBus = new EventBus();

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
}
