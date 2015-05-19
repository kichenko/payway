/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

/**
 * ConfigurationApplyServiceCallback
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
public interface ConfigurationApplyCallback {

    public enum ProgressStep {

        FileCopy,
        DbRefresh,
        SeverRefresh
    }

    public enum ReasonCode {

        Unknown,
        AlreadyInUse
    }

    void start();

    boolean progress(ProgressStep step, Object arg);

    void success();

    void fail(ReasonCode code);

    void finish();
}
