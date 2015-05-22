/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.notification;

/**
 * NotificationEvent
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public interface NotificationEvent {

    NotificationEventPriorityType getPriority();

    NotificationEventType getKind();
}
