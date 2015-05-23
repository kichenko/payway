/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

/**
 * SettingsAppService
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public interface SettingsAppService {

    String getLocalConfigPath();

    int getUploadBufferSize();

    String getTemporaryFileExt();

    String getContextPath();

    void setContextPath(String contextPath);

    String getServerConfigPath();

    void setServerConfigPath(String serverConfigPath);
}
