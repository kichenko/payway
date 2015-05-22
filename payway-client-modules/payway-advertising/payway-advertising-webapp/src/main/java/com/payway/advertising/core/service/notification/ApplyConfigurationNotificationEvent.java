/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.notification;

import com.payway.advertising.core.service.config.apply.ApplyStatus;
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
public class ApplyConfigurationNotificationEvent extends AbstractNotificationEvent {

    private String userName;
    private LocalDateTime dateCreate;
    private LocalDateTime dateStatus;
    private ApplyStatus status;

    public ApplyConfigurationNotificationEvent() {
        setKind(NotificationEventType.ApplyConfiguration);
        setPriority(NotificationEventPriorityType.High);
    }

    public ApplyConfigurationNotificationEvent(String userName, LocalDateTime dateCreate, ApplyStatus status, LocalDateTime dateStatus) {
        setKind(NotificationEventType.ApplyConfiguration);
        setPriority(NotificationEventPriorityType.High);
        setUserName(userName);
        setDateCreate(dateCreate);
        setStatus(status);
        setDateStatus(dateStatus);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        return (obj instanceof ApplyConfigurationNotificationEvent) && (getKind().equals(((ApplyConfigurationNotificationEvent) obj).getKind()) && getPriority().equals(((ApplyConfigurationNotificationEvent) obj).getPriority()));
    }

    @Override
    public int hashCode() {
        return kind.hashCode() + priority.hashCode();
    }

}
