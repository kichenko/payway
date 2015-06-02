package com.payway.advertising.web;

import com.payway.advertising.web.event.ApplicationStartEvent;
import javax.servlet.ServletContextEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by mike on 20/05/15.
 */
@Slf4j
public class SpringContextInitializer extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext wac = initWebApplicationContext(event.getServletContext());
        wac.publishEvent(new ApplicationStartEvent(this));
    }
}
