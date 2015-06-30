/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.container.impl;

import com.payway.media.core.codec.Codec;
import com.payway.media.core.codec.impl.LibMp3LameCodec;
import com.payway.media.core.codec.impl.LibX264Codec;
import com.payway.media.core.codec.impl.Mpeg4Codec;
import com.payway.media.core.container.AbstractFormatContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mp4FormatContainer
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class Mp4FormatContainer extends AbstractFormatContainer {

    private static final long serialVersionUID = -3306663135539103667L;

    private static final List<Codec> SUPPORTED_CODECS = new ArrayList<>();

    static {
        //audio encoder
        SUPPORTED_CODECS.add(new LibMp3LameCodec());

        //video encoder
        SUPPORTED_CODECS.add(new LibX264Codec());
        SUPPORTED_CODECS.add(new Mpeg4Codec());
    }

    public Mp4FormatContainer() {
        id = "mp4";
        name = "Mpeg4";
    }

    @Override
    public List<Codec> getSupportedCodecs() {
        return Collections.unmodifiableList(SUPPORTED_CODECS);
    }

    @Override
    public boolean isCodecSupported(Codec codec) {
        return SUPPORTED_CODECS.contains(codec);
    }

    @Override
    public String getFileExt() {
        return "mp4";
    }
}
