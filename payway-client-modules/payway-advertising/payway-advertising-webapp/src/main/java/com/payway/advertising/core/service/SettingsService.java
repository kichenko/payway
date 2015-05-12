/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

/**
 * SettingsService
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public interface SettingsService {

    String getLocalConfigPath();
    String getSeparator();
    int getUploadBufferSize();
}
