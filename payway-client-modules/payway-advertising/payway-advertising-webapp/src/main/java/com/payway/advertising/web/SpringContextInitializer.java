package com.payway.advertising.web;

import com.payway.commons.webapp.web.event.ApplicationStartEvent;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainerFormat;
import javax.servlet.ServletContextEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by mike on 20/05/15.
 */
@Slf4j
public class SpringContextInitializer extends ContextLoaderListener {

    private static final String inputFilename = System.getProperty("user.home") + "//input.avi";//"c://users//sergey//input.avi";
    private static final String outputFilename = System.getProperty("user.home") + "//output.ogv";//"c://users//sergey//output.ogv";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext wac = initWebApplicationContext(event.getServletContext());
        wac.publishEvent(new ApplicationStartEvent(this));

        transcode();
    }

    private void transcode() {
        
        log.debug("xuggler start");

        // create a media reader  
        IMediaReader mediaReader = ToolFactory.makeReader(/*XugglerIO.map(new FileInputStream(new File(*/inputFilename/*)))*/);

        // create a media writer  
        IMediaWriter mediaWriter = ToolFactory.makeWriter(/*XugglerIO.map(new FileOutputStream(new File(*/outputFilename/*)))*/, mediaReader);

        IContainerFormat containerFormat = IContainerFormat.make();
        containerFormat.setOutputFormat("ogv", "output.ogv", "video/ogv");
        mediaWriter.getContainer().setFormat(containerFormat);

        //containerFormat.s
        // add a writer to the reader, to create the output file  
        mediaReader.addListener(mediaWriter);
        
        log.debug("xuggler middle");

        int z = 900;
        // read and decode packets from the source file and  
        // and dispatch decoded audio and video to the writer  
        while (mediaReader.readPacket() == null);

        int k = 900;
        
        log.debug("xuggler end");
    }
}
