/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification.events.report;

import com.payway.advertising.ui.component.notification.events.common.AbstractNotificationEvent;
import com.payway.advertising.ui.component.notification.events.common.NotificationEventPriorityType;
import com.payway.advertising.ui.component.notification.events.common.NotificationEventType;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

/**
 * ExecuteReportNotificationEvent
 *
 * @author Sergey Kichenko
 * @created 14.08.2015
 */
@Getter
@Setter
public final class ExecuteReportNotificationEvent extends AbstractNotificationEvent {

    private static final long serialVersionUID = 8126154874427114922L;

    @Setter(AccessLevel.PROTECTED)
    private UUID uid;

    @Setter(AccessLevel.PROTECTED)
    private String name;

    private LocalDateTime start;
    private LocalDateTime statusDate;
    private ExecuteReportStatusType status;

    public ExecuteReportNotificationEvent() {
        setKind(NotificationEventType.ExecuteReport);
        setPriority(NotificationEventPriorityType.Medium);
    }

    public ExecuteReportNotificationEvent(UUID uid, String name, String userName, LocalDateTime start, LocalDateTime statusDate, ExecuteReportStatusType status, Object... args) {
        this();
        setUid(uid);
        setName(name);
        setArgs(args);
        setStart(start);
        setStatus(status);
        setStatusDate(statusDate);
        setUserName(userName);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        return (obj instanceof ExecuteReportNotificationEvent)
                && (getKind().equals(((ExecuteReportNotificationEvent) obj).getKind())
                && getPriority().equals(((ExecuteReportNotificationEvent) obj).getPriority())
                && getUid().equals(((ExecuteReportNotificationEvent) obj).getUid()));
    }

    @Override
    public int hashCode() {
        return getKind().hashCode() + getPriority().hashCode() + getUid().hashCode() + getName().hashCode();
    }
}
