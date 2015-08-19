/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.storage.impl;

import com.payway.webapp.settings.db.model.DbWebAppUserSettings;
import com.payway.webapp.settings.exception.SettingsStorageException;
import com.payway.webapp.settings.repository.WebAppUserSettingsRepository;
import com.payway.webapp.settings.storage.SettingsStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DataBaseSettingsStorageService
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@Component(value = "app.settings.DataBaseSettingsStorageService")
public class DataBaseSettingsStorageService implements SettingsStorageService {

    @Autowired
    private WebAppUserSettingsRepository repository;

    @Override
    @Transactional(noRollbackFor = {Exception.class})
    public void save(DbWebAppUserSettings settings) throws SettingsStorageException {
        repository.save(settings);
    }

    @Override
    @Transactional(readOnly = true)
    public DbWebAppUserSettings load(String appId, String login, String key) throws SettingsStorageException {
        return repository.findByAppIdAndLoginAndKey(appId, login, key);
    }
}
