/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.xuggle;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import java.net.Socket;
import org.testng.annotations.Test;

/**
 * XuggleTest
 *
 * @author Sergey Kichenko
 * @created 25.06.15 00:00
 */
public class XuggleTest {

    private static final String inputFilename = "c://users//sergey//input.avi";
    private static final String outputFilename = "c://users//sergey//output.mp4";

    @Test
    public void testTranscode() throws Exception {
        
        Socket echoSocket        
        
        // create a media reader  
        IMediaReader mediaReader = ToolFactory.makeReader(/*XugglerIO.map(new FileInputStream(new File(*/inputFilename/*)))*/);
        
        IContainer c = mediaReader.getContainer();
        IContainerFormat inFmt = mediaReader.getContainer().getFormat();

        // create a media writer  
        IMediaWriter mediaWriter = ToolFactory.makeWriter(/*XugglerIO.map(new FileOutputStream(new File(*/outputFilename/*)))*/, mediaReader);

        IContainerFormat containerFormat = IContainerFormat.make();
        containerFormat.setOutputFormat("webm", "output.mp4", "video/mp4");
        mediaWriter.getContainer().setFormat(containerFormat);

        // add a writer to the reader, to create the output file  
        mediaReader.addListener(mediaWriter);

        int z = 900;
        // read and decode packets from the source file and  
        // and dispatch decoded audio and video to the writer  
        while (mediaReader.readPacket() == null);

        int k = 900;
    }
}
