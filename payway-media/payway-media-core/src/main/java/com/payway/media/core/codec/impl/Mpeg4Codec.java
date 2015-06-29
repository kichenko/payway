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
 * Mpeg4Codec
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class Mpeg4Codec extends AbstractCodec {

    private static final long serialVersionUID = -1446858388169867739L;

    public Mpeg4Codec() {
        id = "mpeg4";
        shortName = "";
        longName = "";
        codecType = CodecType.Video;
        codecDirection = CodecDirection.Encoder;
    }

}
