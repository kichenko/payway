/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.utils;

/**
 * SettingsAppService
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public interface SettingsAppService {

    String getLocalConfigPath();

    String getSeparator();

    int getUploadBufferSize();

    String getTemporaryFileExt();

    String getContextPath();

    void setContextPath(String contextPath);
}
