/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

import lombok.Getter;
import org.joda.time.LocalDateTime;

/**
 * ApplyConfigurationStatus
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
@Getter
public final class ApplyConfigurationStatus {

    public enum Step {

        None,
        Prepare,
        Canceling,
        Cancel,
        CopyFiles,
        UpdateDatabase,
        Confirmation,
        Success,
        Fail
    }

    private final String login;
    private final LocalDateTime startTime;
    private final Step step;
    private final Object[] args;

    public ApplyConfigurationStatus(String login, LocalDateTime startTime, Step step, Object... args) {
        this.login = login;
        this.startTime = startTime;
        this.step = step;
        this.args = args;
    }

    public ApplyConfigurationStatus(String login, LocalDateTime startTime, Step step) {
        this.login = login;
        this.startTime = startTime;
        this.step = step;
        this.args = null;
    }
}
