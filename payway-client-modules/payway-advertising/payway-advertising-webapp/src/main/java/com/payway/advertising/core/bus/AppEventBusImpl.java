/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.bus;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

/**
 * AppEventBusImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Component(value = "appEventBus")
public class AppEventBusImpl implements AppEventBus {

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
    public void sendNotification(AppBusEvent event) {
        eventBus.post(event);
    }

}
