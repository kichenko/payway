/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings;

import com.payway.webapp.settings.exception.SettingsException;

/**
 * WebAppSettingsService
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
public interface WebAppSettingsService extends SettingsService {

    void save(String login, String key, Object value) throws SettingsException;

    Object load(String login, String key) throws SettingsException;

    boolean exist(String login, String key);
}
