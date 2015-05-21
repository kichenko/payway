/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.notification;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * NotificationServiceImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
@UIScope
@Component(value = "notificationService")
public class NotificationServiceImpl implements NotificationService, SubscriberExceptionHandler {

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
    public void sendNotification(NotificationEvent event) {
        eventBus.post(event);
    }

    @Override
    public void handleException(Throwable exception, SubscriberExceptionContext context) {
        log.error("Error in notification event bus", exception);
    }
}
