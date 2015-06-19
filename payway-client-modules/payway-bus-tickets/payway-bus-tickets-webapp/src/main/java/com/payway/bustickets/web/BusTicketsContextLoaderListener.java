/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.web;

import com.payway.web.event.ApplicationStartEvent;
import javax.servlet.ServletContextEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * BuTicketsContextLoaderListener
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Slf4j
public class BusTicketsContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext wac = initWebApplicationContext(event.getServletContext());
        wac.publishEvent(new ApplicationStartEvent(this));
    }
}
