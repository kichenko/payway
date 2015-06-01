/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

import com.payway.advertising.messaging.MessageServerSenderService;
import com.payway.advertising.messaging.ResponseCallbackSupport;
import com.payway.advertising.web.ApplicationOnStartClientConnectedEvent;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.message.SettingsRequest;
import com.payway.messaging.message.SettingsResponse;
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
@Component(value = "settingsAppService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SettingsAppServiceImpl implements SettingsAppService, ApplicationListener<ApplicationOnStartClientConnectedEvent> {

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

    /*
     @Autowired
     @Qualifier("Cluster.Topic.Config")
     ITopic<ConfigMessage> topic;
     */
    @Override
    public void onApplicationEvent(ApplicationOnStartClientConnectedEvent event) {
        log.debug("Loading settings from cluster");
        sender.sendMessage(new SettingsRequest(), new ResponseCallbackSupport<SettingsResponse, ExceptionResponse>() {
            @Override
            public void onServerResponse(SettingsResponse response) {
                String cp = response.getSettings().getConfigPath();
                log.debug("configPath: {}", cp);
                setServerConfigPath(cp);
            }
        });
    }

    /*
     @PostConstruct
     public void onPostConstruct() {
     topic.addMessageListener(new MessageListener<ConfigMessage>() {
     @Override
     public void onMessage(Message<ConfigMessage> message) {
     ConfigMessage cm = message.getMessageObject();
     log.debug("Config {} = {} [{}]", cm.getConfigName(), cm.getValueName());
     }
     });
     }
     */
}
