/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

import com.google.common.eventbus.Subscribe;
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
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
}
