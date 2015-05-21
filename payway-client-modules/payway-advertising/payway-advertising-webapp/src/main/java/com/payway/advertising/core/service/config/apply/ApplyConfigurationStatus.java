/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.config.apply;

import lombok.Getter;

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
        Start,
        Canceling,
        Cancel,
        CopyFiles,
        UpdateDatabase,
        Confirmation,
        Success,
        Fail,
        Finish
    }

    //private final String login;
    //private final LocalDateTime dateCreated;
    private final Step step;
    private final Object[] args;

    public ApplyConfigurationStatus(Step step, Object... args) {
        this.step = step;
        this.args = args;
    }

    public ApplyConfigurationStatus(Step step) {
        this.step = step;
        this.args = null;
    }
}
