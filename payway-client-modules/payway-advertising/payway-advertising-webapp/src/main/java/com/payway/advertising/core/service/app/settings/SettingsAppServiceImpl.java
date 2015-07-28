/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
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
import com.payway.media.core.codec.Codec;
import com.payway.media.core.codec.CodecDirection;
import com.payway.media.core.codec.CodecType;
import com.payway.media.core.container.FormatContainer;
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
@Component(value = "app.advertising.SettingsAppService")
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

    @Value("${app.media.video.converting.default.format}")
    private String appMediaVideoConvertingDefaultFormat;

    @Value("${app.media.video.converting.default.format.codec.encoder.audio}")
    private String appMediaVideoConvertingDefaultFormatCodecEncoderAudio;

    @Value("${app.media.video.converting.default.format.codec.encoder.video}")
    private String appMediaVideoConvertingDefaultFormatCodecEncoderVideo;

    @Value("${app.media.video.converting.default.format.codec.encoder.video.bitrate}")
    private int appMediaVideoConvertingDefaultFormatCodecEncoderVideoBitrate;

    @Value("${app.media.video.converting.enable}")
    private boolean convertVideoFiles;

    @Value("")
    private String contextPath;

    @Value("")
    private String serverConfigPath;

    @Autowired
    private ContainerService formatContainerService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageServerSenderService sender;

    private FormatContainer currentFormatContainer;

    private List<FormatContainer> supportedFormatContainers;

    private VideoAttributes videoAttributes;

    private AudioAttributes audioAttributes;

    @PostConstruct
    public void postConstruct() {

        supportedFormatContainers = formatContainerService.getSupportedFormatContainers();

        audioAttributes = new AudioAttributes(null, 0, 0, 0, 0, 0, null);
        videoAttributes = new VideoAttributes(null, null, appMediaVideoConvertingDefaultFormatCodecEncoderVideoBitrate * 1000, 0, 0, 0, null);

        for (FormatContainer fmt : supportedFormatContainers) {
            if (appMediaVideoConvertingDefaultFormat.equals(fmt.getId())) {
                currentFormatContainer = fmt;
                break;
            }
        }

        /**
         *
         * Set defaults audio & video codec encoders, if it's possible
         *
         */
        if (currentFormatContainer != null) {

            List<Codec> supportedCodecs = currentFormatContainer.getSupportedCodecs();

            List<Codec> audioEncoder = FluentIterable.from(supportedCodecs).filter(new Predicate<Codec>() {
                @Override
                public boolean apply(Codec codec) {
                    return CodecDirection.Encoder.equals(codec.getDirection()) && CodecType.Audio.equals(codec.getType());
                }
            }).toList();

            List<Codec> videoEncoder = FluentIterable.from(supportedCodecs).filter(new Predicate<Codec>() {
                @Override
                public boolean apply(Codec codec) {
                    return CodecDirection.Encoder.equals(codec.getDirection()) && CodecType.Video.equals(codec.getType());
                }
            }).toList();

            if (getAudioAttributes() != null) {
                for (Codec codec : audioEncoder) {
                    if (codec.getId().equals(appMediaVideoConvertingDefaultFormatCodecEncoderAudio)) {
                        getAudioAttributes().setCodec(codec);
                        break;
                    }
                }
            }

            if (getVideoAttributes() != null) {
                for (Codec codec : videoEncoder) {
                    if (codec.getId().equals(appMediaVideoConvertingDefaultFormatCodecEncoderVideo)) {
                        getVideoAttributes().setCodec(codec);
                        break;
                    }
                }
            }
        }

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

        /**
         * * Set defaults audio & video codec encoders, if it's possible
         */
        if (currentFormatContainer != null) {

            if (getAudioAttributes() != null) {
                getAudioAttributes().setCodec(formatContainerService.getDefaultAudioEncoderCodec(currentFormatContainer));
            }

            if (getVideoAttributes() != null) {
                getVideoAttributes().setCodec(formatContainerService.getDefaultVideoEncoderCodec(currentFormatContainer));
            }
        }
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
