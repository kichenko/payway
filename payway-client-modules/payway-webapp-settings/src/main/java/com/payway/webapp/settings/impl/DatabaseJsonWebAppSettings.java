/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.impl;

import com.payway.webapp.settings.WebAppSettingsService;
import com.payway.webapp.settings.db.model.DbWebAppUserSettings;
import com.payway.webapp.settings.exception.SettingsException;
import com.payway.webapp.settings.model.Setting;
import com.payway.webapp.settings.storage.SettingsStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * DatabaseWebAppSettings
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@Component(value = "app.settings.DatabaseJsonWebAppSettings")
public class DatabaseJsonWebAppSettings implements WebAppSettingsService {

    @Value("${app.id}")
    private String appId;

    @Autowired
    @Qualifier(value = "app.settings.WebAppSetting2JsonConvertor")
    private Converter<Setting, String> serializer;

    @Autowired
    @Qualifier(value = "app.settings.Json2WebAppSettingConvertor")
    private Converter<String, Setting> deserializer;

    @Autowired
    @Qualifier(value = "app.settings.DataBaseSettingsStorageService")
    private SettingsStorageService storage;

    @Override
    public void save(String login, String key, Setting value) throws SettingsException {
        save(appId, login, key, value);
    }

    @Override
    public Setting load(String login, String key) throws SettingsException {
        return load(appId, login, key);
    }

    @Override
    public void save(String appId, String login, String key, Setting value) throws SettingsException {
        try {
            storage.save(new DbWebAppUserSettings(appId, login, key, serializer.convert(value)));
        } catch (Exception ex) {
            throw new SettingsException(ex.getMessage(), ex);
        }
    }

    @Override
    public Setting load(String appId, String login, String key) throws SettingsException {
        try {
            return deserializer.convert(storage.load(appId, login, key).getValue());
        } catch (Exception ex) {
            throw new SettingsException(ex.getMessage(), ex);
        }
    }
}
