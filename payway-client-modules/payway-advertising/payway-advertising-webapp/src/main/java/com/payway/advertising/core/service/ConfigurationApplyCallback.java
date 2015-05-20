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

    void progress(final ApplyConfigurationStatus status);
}
