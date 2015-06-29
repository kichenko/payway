/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.converter.video;

import com.payway.media.core.attributes.audio.AudioAttributes;
import com.payway.media.core.attributes.video.VideoAttributes;
import com.payway.media.core.container.FormatContainer;
import com.payway.media.core.converter.Converter;
import com.payway.media.core.exception.MediaException;
import java.io.File;
import java.util.Properties;

/**
 * VideoConverter
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public interface VideoConverter extends Converter {

    void convert(File inputFile, File outputFile, FormatContainer formatContainer, VideoAttributes videoAttributes, AudioAttributes audioAttributes, Properties cpreset) throws MediaException;
}
