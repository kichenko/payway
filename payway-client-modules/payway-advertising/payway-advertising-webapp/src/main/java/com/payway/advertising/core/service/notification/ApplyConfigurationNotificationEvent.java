/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.notification;

import com.payway.advertising.core.service.config.apply.ApplyConfigurationStatus;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

/**
 * AppyConfigurationNotificationEvent
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public class ApplyConfigurationNotificationEvent extends AbstractNotificationEvent {

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private LocalDateTime dateCreate;

    @Getter
    @Setter
    private ApplyConfigurationStatus.Step step;

    @Override
    public EventType getKind() {
        return EventType.Fixed;
    }
}
