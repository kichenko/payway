/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.jave;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.FFMPEGLocator;
import it.sauronsoftware.jave.VideoAttributes;
import it.sauronsoftware.jave.VideoSize;
import java.io.File;
import org.testng.annotations.Test;

/**
 * JaveTest
 *
 * @author Sergey Kichenko
 * @created 25.06.15 00:00
 */
public class JaveTest {

    private static final String inputFilename = "c://users//sergey//input.avi";
    private static final String outputFilename = "c://users//sergey//output.3gp";

    @Test
    public void test() throws Exception {

        File source = new File(inputFilename);
        File target = new File(outputFilename);

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        audio.setBitRate(128000);
        audio.setSamplingRate(44100);
        audio.setChannels(2);

        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(160000);
        video.setFrameRate(15);
        video.setSize(new VideoSize(176, 144));

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("3gp");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);

        Encoder encoder = new Encoder(new FFMPEGLocator() {

            @Override
            protected String getFFMPEGExecutablePath() {
                return "c:\\users\\sergey\\ffmpeg.exe";
            }
        });

        encoder.encode(source, target, attrs);
    }
}
