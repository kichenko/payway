/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.storage;

import com.payway.webapp.settings.db.model.DbWebAppUserSettings;
import com.payway.webapp.settings.exception.SettingsStorageException;

/**
 * SettingsStorageService
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
public interface SettingsStorageService {

    void save(DbWebAppUserSettings settings) throws SettingsStorageException;

    DbWebAppUserSettings load(String appId, String login, String key) throws SettingsStorageException;
    
    boolean exist(String appId, String login, String key);
}
