/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.settings;

import com.payway.media.core.attributes.audio.AudioAttributes;
import com.payway.media.core.attributes.video.VideoAttributes;
import com.payway.media.core.container.FormatContainer;
import java.util.List;

/**
 * SettingsAppService
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public interface SettingsAppService extends DatabaseAppSettings {

    String getLocalConfigPath();

    int getUploadBufferSize();

    String getTemporaryFileExt();

    String getTemporaryUploadDirPath();

    String getContextPath();

    void setContextPath(String contextPath);

    String getServerConfigPath();

    void setServerConfigPath(String serverConfigPath);

    FormatContainer getCurrentFormatContainer();

    void setCurrentFormatContainer(FormatContainer currentFormatContainer);

    List<FormatContainer> getSupportedFormatContainers();

    void setSupportedFormatContainers(List<FormatContainer> supportedFormatContainers);

    VideoAttributes getVideoAttributes();

    void setVideoAttributes(VideoAttributes videoAttributes);

    AudioAttributes getAudioAttributes();

    void setAudioAttributes(AudioAttributes audioAttributes);

    boolean isConvertVideoFiles();

    void setConvertVideoFiles(boolean flag);
}
