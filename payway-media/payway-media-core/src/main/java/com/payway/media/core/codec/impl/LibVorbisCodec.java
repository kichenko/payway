 /*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.codec.impl;

import com.payway.media.core.codec.AbstractCodec;
import com.payway.media.core.codec.CodecDirection;
import com.payway.media.core.codec.CodecType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * LibVorbisCodec
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class LibVorbisCodec extends AbstractCodec {

    private static final long serialVersionUID = 1194354900956535544L;

    public LibVorbisCodec() {
        id = "libvorbis";
        shortName = "";
        longName = "";
        codecType = CodecType.Audio;
        codecDirection = CodecDirection.Encoder;
    }
}
