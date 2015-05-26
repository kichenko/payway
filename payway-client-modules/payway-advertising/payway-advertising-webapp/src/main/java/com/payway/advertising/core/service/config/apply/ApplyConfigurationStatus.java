/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

import lombok.Getter;
import lombok.ToString;
import org.joda.time.LocalDateTime;

/**
 * ApplyConfigurationStatus
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
@Getter
@ToString
public final class ApplyConfigurationStatus {

    private final String login;
    private final LocalDateTime startTime;
    private final ApplyStatus status;
    private final LocalDateTime statusTime;
    private final Object[] args;

    public ApplyConfigurationStatus(String login, LocalDateTime startTime, ApplyStatus status, LocalDateTime statusTime, Object... args) {
        this.login = login;
        this.startTime = startTime;
        this.statusTime = statusTime;
        this.status = status;
        this.args = args;
    }

    public ApplyConfigurationStatus(String login, LocalDateTime startTime, ApplyStatus status, LocalDateTime statusTime) {
        this.login = login;
        this.startTime = startTime;
        this.statusTime = statusTime;
        this.status = status;
        this.args = null;
    }
}
