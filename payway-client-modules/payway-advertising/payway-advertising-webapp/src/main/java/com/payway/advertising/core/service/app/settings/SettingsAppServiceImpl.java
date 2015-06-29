/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.core.service.ConfigurationService;
import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.common.DbConfiguration;
import com.payway.advertising.model.common.DbConfigurationKeyType;
import com.payway.commons.webapp.config.SubscribeOnAppEventBus;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.ResponseCallbackSupport;
import com.payway.commons.webapp.web.event.ApplicationStartClientConnectedEvent;
import com.payway.media.core.attributes.audio.AudioAttributes;
import com.payway.media.core.attributes.video.VideoAttributes;
import com.payway.media.core.codec.impl.LibTheoraCodec;
import com.payway.media.core.codec.impl.LibVorbisCodec;
import com.payway.media.core.container.FormatContainer;
import com.payway.media.core.container.impl.OggFormatContainer;
import com.payway.media.core.container.service.ContainerService;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.message.SettingsChangedMessage;
import com.payway.messaging.message.advertising.AdvertisingSettingsRequest;
import com.payway.messaging.message.advertising.AdvertisingSettingsResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Value("${app.config.local.path}")
    private String localConfigPath;

    @Value("${app.config.upload.buf.size}")
    private int uploadBufferSize;

    @Value("${app.config.upload.tmp.file.ext}")
    private String temporaryFileExt;

    @Value("${app.config.upload.tmp.dir.path}")
    private String temporaryUploadDirPath;

    @Value("")
    private String contextPath;

    @Value("")
    private String serverConfigPath;

    @Autowired
    @Qualifier(value = "formatContainerService")
    private ContainerService formatContainerService;

    @Autowired
    private ConfigurationService configurationService;

    private FormatContainer currentFormatContainer;

    private List<FormatContainer> supportedFormatContainers;

    private VideoAttributes videoAttributes;

    private AudioAttributes audioAttributes;

    private boolean convertVideoFiles;

    @PostConstruct
    public void postConstruct() {

        currentFormatContainer = new OggFormatContainer();
        supportedFormatContainers = formatContainerService.getSupportedFormatContainers();

        audioAttributes = new AudioAttributes(new LibVorbisCodec(), 0, 0, 0, 0, 0, null);
        videoAttributes = new VideoAttributes(new LibTheoraCodec(), null, 1400 * 1000, 0, 0, 0, null);

        try {
            load();
        } catch (Exception ex) {
            log.error("Could not load app settings from database - {}", ex);
        }
    }

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

    @Override
    public String getTemporaryUploadDirPath() {
        return temporaryUploadDirPath;
    }

    @Override
    public FormatContainer getCurrentFormatContainer() {
        return currentFormatContainer;
    }

    @Override
    public VideoAttributes getVideoAttributes() {
        return videoAttributes;
    }

    @Override
    public AudioAttributes getAudioAttributes() {
        return audioAttributes;
    }

    @Override
    public List<FormatContainer> getSupportedFormatContainers() {
        return supportedFormatContainers;
    }

    @Override
    public void setCurrentFormatContainer(FormatContainer currentFormatContainer) {
        this.currentFormatContainer = currentFormatContainer;
    }

    @Override
    public void setSupportedFormatContainers(List<FormatContainer> supportedFormatContainers) {
        this.supportedFormatContainers = supportedFormatContainers;
    }

    @Override
    public void setVideoAttributes(VideoAttributes videoAttributes) {
        this.videoAttributes = videoAttributes;
    }

    @Override
    public void setAudioAttributes(AudioAttributes audioAttributes) {
        this.audioAttributes = audioAttributes;
    }

    @Override
    public boolean isConvertVideoFiles() {
        return convertVideoFiles;
    }

    @Override
    public void setConvertVideoFiles(boolean flag) {
        convertVideoFiles = flag;
    }

    @Override
    public void save() throws ServiceException {

        List<DbConfiguration> configs = new ArrayList<>(3);
        configs.add(new DbConfiguration(DbConfigurationKeyType.AdvertisingConvertVideoEnable, Boolean.toString(isConvertVideoFiles())));
        configs.add(new DbConfiguration(DbConfigurationKeyType.AdvertisingConvertVideoBitRate, Integer.toString(getVideoAttributes().getBitRate())));
        configs.add(new DbConfiguration(DbConfigurationKeyType.AdvertisingConvertVideoFormatContainer, getCurrentFormatContainer().getId()));

        configurationService.save(configs);
    }

    @Override
    public void load() throws ServiceException {

        List<DbConfigurationKeyType> keys = new ArrayList<>(3);

        Collections.addAll(keys,
          DbConfigurationKeyType.AdvertisingConvertVideoEnable,
          DbConfigurationKeyType.AdvertisingConvertVideoFormatContainer,
          DbConfigurationKeyType.AdvertisingConvertVideoBitRate
        );

        List<DbConfiguration> configs = configurationService.getByKeys(keys);
        for (DbConfiguration config : configs) {
            if (DbConfigurationKeyType.AdvertisingConvertVideoEnable.equals(config.getKey())) {
                setConvertVideoFiles(Boolean.parseBoolean(config.getValue()));
            } else if (DbConfigurationKeyType.AdvertisingConvertVideoBitRate.equals(config.getKey())) {
                if (getVideoAttributes() != null) {
                    getVideoAttributes().setBitRate(Integer.parseInt(config.getValue()));
                }
            } else if (DbConfigurationKeyType.AdvertisingConvertVideoFormatContainer.equals(config.getKey()) && !StringUtils.isBlank(config.getValue())) {
                for (FormatContainer fmt : getSupportedFormatContainers()) {
                    if (fmt.getId().equals(config.getValue())) {
                        setCurrentFormatContainer(fmt);
                        break;
                    }
                }
            }
        }
    }
}
