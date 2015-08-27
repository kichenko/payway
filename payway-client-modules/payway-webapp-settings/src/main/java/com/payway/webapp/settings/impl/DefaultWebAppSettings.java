/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.impl;

import com.payway.webapp.settings.WebAppSettingsService;
import com.payway.webapp.settings.db.model.DbWebAppUserSettings;
import com.payway.webapp.settings.exception.SettingsException;
import com.payway.webapp.settings.storage.SettingsStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

/**
 * DefaultWebAppSettings - write/read with database and json
 * seriazile/deserialize.
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@Slf4j
@Component(value = "app.settings.DefaultWebAppSettings")
public class DefaultWebAppSettings implements WebAppSettingsService {

    @Value("${app.id}")
    private String appId;

    @Autowired
    @Qualifier(value = "app.settings.WebAppSettingsConversionService")
    private ConversionService conversionService;

    @Autowired
    @Qualifier(value = "app.settings.DataBaseSettingsStorageService")
    private SettingsStorageService storage;

    @Override
    public void save(String login, String key, Object value) throws SettingsException {
        save(appId, login, key, value);
    }

    @Override
    public Object load(String login, String key) throws SettingsException {
        return load(appId, login, key);
    }

    @Override
    public void save(String appId, String login, String key, Object value) throws SettingsException {

        try {
            String content = conversionService.convert(value, String.class);
            DbWebAppUserSettings entity = storage.load(appId, login, key);
            if (entity == null) {
                entity = new DbWebAppUserSettings(appId, login, value.getClass().getName(), key, content);
            } else {
                if (content.equals(entity.getValue())) {
                    log.debug("Equals web app settings, skip saving [appId={}, login={}, key={}]", appId, login, key);
                    return;
                }
                entity.setValue(content);
            }
            storage.save(entity);
        } catch (Exception ex) {
            throw new SettingsException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object load(String appId, String login, String key) throws SettingsException {
        try {
            DbWebAppUserSettings entity = storage.load(appId, login, key);
            return entity == null ? null : conversionService.convert(entity.getValue(), Class.forName(entity.getClassName()));
        } catch (Exception ex) {
            throw new SettingsException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean exist(String appId, String login, String key) {
        return storage.exist(appId, login, key);
    }

    @Override
    public boolean exist(String login, String key) {
        return storage.exist(appId, login, key);
    }
}
