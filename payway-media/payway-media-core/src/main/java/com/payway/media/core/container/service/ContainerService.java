/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.container.service;

import com.payway.media.core.codec.Codec;
import com.payway.media.core.container.FormatContainer;
import java.util.List;

/**
 * ContainerService
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public interface ContainerService {

    List<FormatContainer> getSupportedFormatContainers();

    List<Codec> getSupportedCodecs(FormatContainer format);

    List<Codec> getSupportedAudioEncoderCodecs(FormatContainer format);

    List<Codec> getSupportedVideoEncoderCodecs(FormatContainer format);

    boolean isContainerSupported(FormatContainer container);

    boolean isCodecSupported(Codec codec);
}
