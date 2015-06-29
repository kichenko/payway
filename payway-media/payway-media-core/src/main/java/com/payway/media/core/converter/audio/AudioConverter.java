/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.converter.audio;

import com.payway.media.core.attributes.audio.AudioAttributes;
import com.payway.media.core.container.FormatContainer;
import com.payway.media.core.converter.Converter;
import com.payway.media.core.exception.MediaException;
import java.io.File;

/**
 * AudioConverter
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public interface AudioConverter extends Converter {

    void convert(File inputFile, File outputFile, FormatContainer formatContainer, AudioAttributes audioAttributes) throws MediaException;
}
