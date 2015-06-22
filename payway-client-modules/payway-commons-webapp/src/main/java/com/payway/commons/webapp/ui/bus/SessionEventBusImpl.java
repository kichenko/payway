/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.bus;

import com.google.common.eventbus.EventBus;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * SessionEventBusImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
public class SessionEventBusImpl implements SessionEventBus, Serializable {

    private static final long serialVersionUID = -5675094820736490733L;

    @Setter
    @Getter
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
    public void sendNotification(Object event) {
        eventBus.post(event);
    }
}
