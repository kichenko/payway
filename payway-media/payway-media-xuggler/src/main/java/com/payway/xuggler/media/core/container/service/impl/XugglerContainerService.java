/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.xuggler.media.core.container.service.impl;

import com.payway.media.core.codec.impl.LibMp3LameCodec;
import com.payway.media.core.codec.impl.LibTheoraCodec;
import com.payway.media.core.codec.impl.LibVorbisCodec;
import com.payway.media.core.codec.impl.LibX264Codec;
import com.payway.media.core.codec.impl.Mpeg4Codec;
import com.payway.media.core.container.impl.Mp4FormatContainer;
import com.payway.media.core.container.impl.OggFormatContainer;
import com.payway.media.core.container.service.AbstractContainerService;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * XugglerContainerService
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class XugglerContainerService extends AbstractContainerService {

    private static final long serialVersionUID = 5093818071216253929L;

    {
        //containers
        supportedContainers.add(new OggFormatContainer());
        supportedContainers.add(new Mp4FormatContainer());

        //codecs
        supportedCodecs.add(new LibMp3LameCodec());
        supportedCodecs.add(new LibTheoraCodec());
        supportedCodecs.add(new LibVorbisCodec());
        supportedCodecs.add(new LibX264Codec());
        supportedCodecs.add(new Mpeg4Codec());
    }
}
