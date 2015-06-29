/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.attributes.video;

import com.payway.media.core.attribures.AbstractAttributes;
import com.payway.media.core.codec.Codec;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * VideoAttributes
 *
 * If attribute value not changed - on converting get default from source codec.
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class VideoAttributes extends AbstractAttributes {

    private static final long serialVersionUID = 3121641180795787036L;

    /**
     * The video size for the encoding process.
     */
    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    public static final class VideoSize {

        /**
         * The video width.
         */
        private final int width;

        /**
         * The video height.
         */
        private final int height;

    }

    /**
     * The codec for the encoding process.
     */
    protected Codec codec;

    /**
     * The video size for the encoding process.
     */
    protected VideoSize size;

    /**
     * The bitrate value for the encoding process.
     */
    protected int bitRate = 0;

    /**
     * Bit rate tolerance the bitstream is allowed to diverge from the reference
     * (in bits) (e.g. 1200000)"
     */
    protected int bitRateTolerance = 0;

    /**
     * The frame rate value for the encoding process.
     */
    protected int frameRate = 0;

    /**
     * Quality setting to use for video. 0 means same as source; higher numbers
     * are (perversely) lower quality. Defaults to 0.
     */
    protected int quality = 0;

    public VideoAttributes(Codec codec, VideoSize size, int bitRate, int bitRateTolerance, int frameRate, int quality, Properties cpreset) {
        setCodec(codec);
        setSize(size);
        setBitRate(bitRate);
        setBitRateTolerance(bitRateTolerance);
        setFrameRate(frameRate);
        setQuality(quality);
        setCpreset(cpreset);
    }
}
