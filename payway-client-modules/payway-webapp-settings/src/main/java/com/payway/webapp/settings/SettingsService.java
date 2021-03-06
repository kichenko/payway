/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings;

import com.payway.webapp.settings.exception.SettingsException;

/**
 * SettingsService
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
public interface SettingsService {

    void save(String appId, String login, String key, Object value) throws SettingsException;

    Object load(String appId, String login, String key) throws SettingsException;

    boolean exist(String appId, String login, String key);
}
