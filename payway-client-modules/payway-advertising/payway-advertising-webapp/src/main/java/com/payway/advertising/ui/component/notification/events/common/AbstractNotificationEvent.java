/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification.events.common;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractNotificationEvent
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public abstract class AbstractNotificationEvent implements NotificationEvent, Serializable {

    private static final long serialVersionUID = 7696145103067455016L;

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    protected Object[] args;

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

    @Override
    public int hashCode() {
        return getKind().hashCode() + getPriority().hashCode();
    }
}
