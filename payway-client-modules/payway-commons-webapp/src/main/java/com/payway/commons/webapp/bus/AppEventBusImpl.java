/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * AppEventBusImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
public class AppEventBusImpl implements AppEventBus {

    @Getter
    @Setter
    private EventBus eventBus;

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
