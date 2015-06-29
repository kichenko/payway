/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.xuggler.core.converter.video.impl;

import com.payway.media.core.attributes.audio.AudioAttributes;
import com.payway.media.core.attributes.video.VideoAttributes;
import com.payway.media.core.codec.impl.LibMp3LameCodec;
import com.payway.media.core.codec.impl.LibTheoraCodec;
import com.payway.media.core.codec.impl.LibVorbisCodec;
import com.payway.media.core.codec.impl.LibX264Codec;
import com.payway.media.core.container.FormatContainer;
import com.payway.media.core.container.impl.Mp4FormatContainer;
import com.payway.media.core.container.impl.OggFormatContainer;
import java.io.File;
import java.util.Properties;
import org.testng.annotations.Test;

/**
 * XugglerVideoConverter
 *
 * @author Sergey Kichenko
 * @created 27.06.15 00:00
 */
public class XugglerVideoConverterTest {

    @Test(enabled = false)
    public void test_ConvertFromInputFile_2_Ogg_With_LibTheora_And_LibVorbis() throws Exception {

        File inputFile = new File("c://src.mp4");
        File outputFile = new File("c://dest.ogv");
        int videoBitRate = 1696 * 1000;

        Properties cpreset = null;
        FormatContainer formatContainer = new OggFormatContainer();
        VideoAttributes videoAttributes = new VideoAttributes(new LibTheoraCodec(), null, videoBitRate, 0, 0, 0, null);
        AudioAttributes audioAttributes = new AudioAttributes(new LibVorbisCodec(), 0, 0, 0, 0, 0, null);

        new XugglerVideoConverter().convert(inputFile, outputFile, formatContainer, videoAttributes, audioAttributes, cpreset);
    }

    @Test(enabled = false)
    public void testConvertFromInputFile_2_Mp4_With_X264_And_LibMp3LameCodec() throws Exception {

        File inputFile = new File("c://src.avi");
        File outputFile = new File("c://dest.mp4");
        int videoBitRate = 800 * 1000;

        Properties cpreset = null;
        FormatContainer formatContainer = new Mp4FormatContainer();
        VideoAttributes videoAttributes = new VideoAttributes(new LibX264Codec(), null, videoBitRate, 0, 0, 0, null);
        AudioAttributes audioAttributes = new AudioAttributes(new LibMp3LameCodec(), 0, 0, 0, 0, 0, null);

        new XugglerVideoConverter().convert(inputFile, outputFile, formatContainer, videoAttributes, audioAttributes, cpreset);
    }
}
