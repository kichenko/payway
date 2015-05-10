/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * SettingsServiceImpl
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
@Component(value = "settingsService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    @Qualifier("localConfigPath")
    private String localConfigPath;

    @Override
    public String getLocalConfigPath() {
        return localConfigPath;
    }

    @Override
    public String getSeparator() {
        return "/";
    }
}
