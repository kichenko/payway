/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.container.impl;

import com.payway.media.core.codec.Codec;
import com.payway.media.core.codec.impl.LibTheoraCodec;
import com.payway.media.core.codec.impl.LibVorbisCodec;
import com.payway.media.core.container.AbstractFormatContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * OggFormatContainer
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class OggFormatContainer extends AbstractFormatContainer {

    private static final long serialVersionUID = -3306663135539103667L;

    private static final List<Codec> SUPPORTED_CODECS = new ArrayList<>();

    static {
        //video encoder
        SUPPORTED_CODECS.add(new LibTheoraCodec());

        //audio encoder
        SUPPORTED_CODECS.add(new LibVorbisCodec());
    }

    public OggFormatContainer() {
        id = "ogg";
        name = "Ogg";
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
        return "ogv";
    }
}
