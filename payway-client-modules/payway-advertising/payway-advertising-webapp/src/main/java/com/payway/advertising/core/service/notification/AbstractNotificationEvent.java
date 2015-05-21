/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.notification;

/**
 * AbstractNotificationEvent
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public abstract class AbstractNotificationEvent implements NotificationEvent {

    protected NotificationAction action;

    @Override
    public NotificationAction getAction() {
        return action;
    }

    @Override
    public void setAction(NotificationAction action) {
        this.action = action;
    }
}
