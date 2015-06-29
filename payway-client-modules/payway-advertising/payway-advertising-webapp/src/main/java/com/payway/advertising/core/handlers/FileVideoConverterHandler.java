/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.utils.Helpers;
import com.payway.media.core.converter.video.VideoConverter;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * FileVideoConverterHandler
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Slf4j
public class FileVideoConverterHandler implements FileHandler {

    private VideoConverter videoConverter;
    private SettingsAppService settingsAppService;
    private List<String> supportedVideoFileExtensions;

    @Override
    public boolean handle(String srcFilePath, String srcFileName, Map<String, Object> params) throws FileHandlerException {

        if (!supportedVideoFileExtensions.contains(StringUtils.substringAfterLast(srcFileName, "."))) {
            log.debug("Could not handle file ext [{}] - not supported", StringUtils.substringAfterLast(srcFileName, "."));
            return false;
        }

        if (!settingsAppService.isConvertVideoFiles()) {
            return false;
        }

        try {

            File inputFile = new File(new URI(Helpers.addEndSeparator(srcFileName) + srcFileName));
            File outputFile = new File(new URI(Helpers.addEndSeparator(srcFileName) + UUID.randomUUID().toString() + "_" + srcFileName));

            videoConverter.convert(inputFile, outputFile, settingsAppService.getCurrentFormatContainer(), settingsAppService.getVideoAttributes(), settingsAppService.getAudioAttributes(), null);

            if (inputFile.delete()) {
                outputFile.renameTo(inputFile);
            } else {
                throw new Exception("Could not rename dst to src file after video convertion - {}");
            }
        } catch (Exception ex) {
            log.error("Could not convert video file - {}", ex);
            throw new FileHandlerException("", ex);
        }

        return true;
    }
}
