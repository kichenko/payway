/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.container;

import com.payway.media.core.codec.Codec;
import java.util.List;

/**
 * FormatContainer
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public interface FormatContainer {

    String getId();

    String getName();

    List<Codec> getSupportedCodecs();

    boolean isCodecSupported(Codec codec);

    String getFileExt();
}
