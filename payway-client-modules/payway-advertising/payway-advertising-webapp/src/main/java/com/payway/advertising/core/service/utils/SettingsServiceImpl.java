/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.utils;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${config.local.path}")
    private String localConfigPath;

    @Value("${config.upload.buf.size}")
    private int uploadBufferSize;

    @Value("${config.upload.tmp.file.ext}")
    private String temporaryFileExt;

    private final String separator = "/";

    @Override
    public String getLocalConfigPath() {
        return localConfigPath;
    }

    @Override
    public String getSeparator() {
        return separator;
    }

    @Override
    public int getUploadBufferSize() {
        return uploadBufferSize;
    }

    @Override
    public String getTemporaryFileExt() {
        return temporaryFileExt;
    }

}
