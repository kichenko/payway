/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.handlers;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.core.utils.Helpers;
import com.payway.media.core.attributes.audio.AudioAttributes;
import com.payway.media.core.attributes.video.VideoAttributes;
import com.payway.media.core.container.FormatContainer;
import com.payway.media.core.converter.video.VideoConverter;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * FileVideoConverterHandler
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Slf4j
@Setter
@Getter
public class FileVideoConverterHandler implements FileHandler {

    /**
     * Proxy bean (prototype scope)
     */
    private VideoConverter videoConverter;

    private SettingsAppService settingsAppService;
    private List<String> supportedVideoFileExtensions;

    @Override
    public boolean handle(FileHandlerArgs args) throws FileHandlerException {

        try {

            String fileExt;
            VideoAttributes videoAttributes;
            AudioAttributes audioAttributes;
            FormatContainer formatContainer;

            log.debug("Start handle file video convertor handler...");
            log.debug("Args = [{}]", args);

            if (!settingsAppService.isConvertVideoFiles()) {
                log.debug("Video converting is not enabled, skip handler");
                return false;
            }

            if (!supportedVideoFileExtensions.contains(StringUtils.substringAfterLast(args.getSrcFileName(), ".").toLowerCase())) {
                log.debug("Could not handle file ext [{}] - not supported", StringUtils.substringAfterLast(args.getSrcFileName(), "."));
                return false;
            }

            if (args == null) {
                throw new Exception("Empty args");
            }

            fileExt = settingsAppService.getCurrentFormatContainer().getFileExt();
            formatContainer = settingsAppService.getCurrentFormatContainer();
            videoAttributes = settingsAppService.getVideoAttributes();
            audioAttributes = settingsAppService.getAudioAttributes();

            File inputFile = new File(new URI(Helpers.addEndSeparator(args.getSrcFilePath()) + args.getSrcFileName()));
            File outputFile = new File(new URI(Helpers.addEndSeparator(args.getSrcFilePath()) + UUID.randomUUID().toString() + "_" + Helpers.changeFileExt(args.getSrcFileName(), fileExt)));

            videoConverter.convert(inputFile, outputFile, formatContainer, videoAttributes, audioAttributes, null);

            //delete input & rename ouput & change file's ext dest
            if (inputFile.delete()) {
                args.setSrcFileName(outputFile.getName());
                args.setDstFileName(Helpers.changeFileExt(args.getDstFileName(), fileExt));
            } else {
                throw new Exception("Could not rename dst to src file after video convertion");
            }
        } catch (Exception ex) {
            log.error("Could not convert video file - ", ex);
            throw new FileHandlerException("", ex);
        } finally {
            log.debug("Stop handle file video convertor handler...");
        }

        return true;
    }
}
