/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.media.core.attributes.audio;

import com.payway.media.core.attribures.AbstractAttributes;
import com.payway.media.core.codec.Codec;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * AudioAttributes
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
public class AudioAttributes extends AbstractAttributes {

    private static final long serialVersionUID = 3343085714907069818L;

    /**
     * The codec for the encoding process.
     */
    protected Codec codec;

    /**
     * The bitrate value for the encoding process.
     */
    protected int bitRate = 0;

    /**
     * The samplingRate value for the encoding process.
     */
    protected int sampleRate = 0;

    /**
     * The channels value (1=mono, 2=stereo) for the encoding process.
     */
    protected int channels = 0;

    /**
     * The volume value (0-256) for the encoding process.
     */
    protected int volume = 0;

    /**
     * Quality setting to use for audio. 0 means same as source; higher numbers
     * are (perversely) lower quality. Defaults to 0.
     */
    protected int quality = 0;

    public AudioAttributes(Codec codec, int bitRate, int sampleRate, int channels, int volume, int quality, Properties cpreset) {
        setCodec(codec);
        setBitRate(bitRate);
        setSampleRate(sampleRate);
        setChannels(channels);
        setQuality(quality);
        setVolume(volume);
        setCpreset(cpreset);
    }
}
