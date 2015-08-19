/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification.events.apply;

import com.payway.advertising.core.service.config.apply.ApplyStatus;
import com.payway.advertising.ui.component.notification.events.common.AbstractNotificationEvent;
import com.payway.advertising.ui.component.notification.events.common.NotificationEventPriorityType;
import com.payway.advertising.ui.component.notification.events.common.NotificationEventType;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

/**
 * AppyConfigurationNotificationEvent
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Getter
@Setter
public final class ApplyConfigurationNotificationEvent extends AbstractNotificationEvent {

    private static final long serialVersionUID = -1434124831498943879L;

    private ApplyStatus status;
    private LocalDateTime dateCreate;
    private LocalDateTime dateStatus;

    public ApplyConfigurationNotificationEvent() {
        setKind(NotificationEventType.ApplyConfiguration);
        setPriority(NotificationEventPriorityType.High);
    }

    public ApplyConfigurationNotificationEvent(String userName, LocalDateTime dateCreate, ApplyStatus status, LocalDateTime dateStatus, Object... args) {
        this();
        setArgs(args);
        setStatus(status);
        setUserName(userName);
        setDateCreate(dateCreate);
        setDateStatus(dateStatus);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        return (obj instanceof ApplyConfigurationNotificationEvent)
                && (getKind().equals(((ApplyConfigurationNotificationEvent) obj).getKind())
                && getPriority().equals(((ApplyConfigurationNotificationEvent) obj).getPriority()));
    }
}
