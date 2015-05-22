package com.payway.advertising.web;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;

/**
 * Created by mike on 20/05/15.
 */
public class SpringContextInitializer extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext wac = initWebApplicationContext(event.getServletContext());
        wac.publishEvent(new ApplicationStartEvent(this));
    }

}
