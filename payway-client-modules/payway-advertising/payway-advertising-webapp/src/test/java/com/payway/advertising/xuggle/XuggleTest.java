/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.xuggle;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import java.util.List;
import org.testng.annotations.Test;

/**
 * XuggleTest
 *
 * @author Sergey Kichenko
 * @created 25.06.15 00:00
 */
public class XuggleTest {

    private static final String inputFilename = "c://users//sergey//1.mp4";
    private static final String outputFilename = "c://users//sergey//output.ogv";

    @Test
    public void testTranscode() throws Exception {

        int code = -1;
        IContainer inputContainer = IContainer.make();

        try {
            code = inputContainer.open(inputFilename, IContainer.Type.READ, null); //open(new FileInputStream(new File(inputFilename)), null, false, true);
        } catch (Exception ex) {
            int k = 900;
        }

        IContainerFormat format = inputContainer.getContainerFormat();

        String value = format.getInputFormatLongName();
        value = format.getInputFormatShortName();

        long bitRate = inputContainer.getBitRate();

        List<ICodec.ID> codecs = format.getOutputCodecsSupported();
        if (codecs != null) {
            for (ICodec.ID c : codecs) {
                value = c.name();
            }
        }

        int numStreams = inputContainer.getNumStreams();
        for (int i = 0; i < numStreams; i++) {
            IStream stream = inputContainer.getStream(i);

            IStreamCoder coder = stream.getStreamCoder();

            System.out.println("*** Start of Stream Info ***");

            System.out.printf("stream %d: ", i);

            System.out.printf("type: %s; ", coder.getCodecType());

            System.out.printf("codec: %s; ", coder.getCodecID());

            System.out.printf("duration: %s; ", stream.getDuration());

            System.out.printf("start time: %s; ", inputContainer.getStartTime());

            System.out.printf("timebase: %d/%d; ",
                    stream.getTimeBase().getNumerator(),
                    stream.getTimeBase().getDenominator());

            System.out.printf("coder tb: %d/%d; ",
                    coder.getTimeBase().getNumerator(),
                    coder.getTimeBase().getDenominator());

            System.out.println();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {

                System.out.printf("sample rate: %d; ", coder.getSampleRate());

                System.out.printf("channels: %d; ", coder.getChannels());

                System.out.printf("format: %s", coder.getSampleFormat());

            } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {

                System.out.printf("width: %d; ", coder.getWidth());

                System.out.printf("height: %d; ", coder.getHeight());

                System.out.printf("format: %s; ", coder.getPixelType());

                System.out.printf("frame-rate: %5.2f; ", coder.getFrameRate().getDouble());

            }

            System.out.println();

            System.out.println("*** End of Stream Info ***");
        }

        // create a media reader  
        IMediaReader mediaReader = ToolFactory.makeReader(/*XugglerIO.map(new FileInputStream(new File(*/inputFilename/*)))*/);

        // create a media writer  
        IMediaWriter mediaWriter = ToolFactory.makeWriter(/*XugglerIO.map(new FileOutputStream(new File(*/outputFilename/*)))*/, mediaReader);

        IContainerFormat containerFormat = IContainerFormat.make();

        containerFormat.setOutputFormat(
                "ogv", "output.ogv", "video/ogv");
        mediaWriter.getContainer()
                .setFormat(containerFormat);

        //containerFormat.s
        // add a writer to the reader, to create the output file  
        mediaReader.addListener(mediaWriter);

        int z = 900;
        // read and decode packets from the source file and  
        // and dispatch decoded audio and video to the writer  

        while (mediaReader.readPacket()
                == null);

        int k = 900;
    }
}
