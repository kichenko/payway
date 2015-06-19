/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.config.SubscribeOnAppEventBus;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.ResponseCallbackSupport;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.message.SettingsChangedMessage;
import com.payway.messaging.message.advertising.AdvertisingSettingsRequest;
import com.payway.messaging.message.advertising.AdvertisingSettingsResponse;
import com.payway.web.event.ApplicationStartClientConnectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * SettingsAppServiceImpl
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
@Slf4j
@SubscribeOnAppEventBus
@Component(value = "settingsAppService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SettingsAppServiceImpl implements SettingsAppService, ApplicationListener<ApplicationStartClientConnectedEvent> {

    @Value("${config.local.path}")
    private String localConfigPath;

    @Value("${config.upload.buf.size}")
    private int uploadBufferSize;

    @Value("${config.upload.tmp.file.ext}")
    private String temporaryFileExt;

    @Value("")
    private String contextPath;

    @Value("")
    private String serverConfigPath;

    @Override
    public String getLocalConfigPath() {
        return localConfigPath;
    }

    @Override
    public int getUploadBufferSize() {
        return uploadBufferSize;
    }

    @Override
    public String getTemporaryFileExt() {
        return temporaryFileExt;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    public String getServerConfigPath() {
        return serverConfigPath;
    }

    @Override
    public void setServerConfigPath(String serverConfigPath) {
        this.serverConfigPath = serverConfigPath;
    }

    @Autowired
    MessageServerSenderService sender;

    @Override
    public void onApplicationEvent(ApplicationStartClientConnectedEvent event) {
        loadRemoteConfiguration();
    }

    private void loadRemoteConfiguration() {
        log.debug("Loading settings from cluster");
        sender.sendMessage(new AdvertisingSettingsRequest(), new ResponseCallbackSupport<AdvertisingSettingsResponse, ExceptionResponse>() {
            @Override
            public void onServerResponse(AdvertisingSettingsResponse response) {
                String cp = response.getSettings().getConfigPath();
                log.debug("configPath: {}", cp);
                setServerConfigPath(cp);
            }
        });
    }

    @Subscribe
    public void onMessage(SettingsChangedMessage message) {
        loadRemoteConfiguration();
    }

}
