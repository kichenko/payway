/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification.events;

import lombok.AccessLevel;
import lombok.Setter;

/**
 * AbstractNotificationEvent
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public abstract class AbstractNotificationEvent implements NotificationEvent {

    @Setter(AccessLevel.PROTECTED)
    protected NotificationEventType kind;

    @Setter(AccessLevel.PROTECTED)
    protected NotificationEventPriorityType priority;

    @Override
    public NotificationEventType getKind() {
        return kind;
    }

    @Override
    public NotificationEventPriorityType getPriority() {
        return priority;
    }
}
