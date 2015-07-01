/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.web;

import com.payway.commons.webapp.web.event.ApplicationStartEvent;
import javax.servlet.ServletContextEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * KioskCashierContextLoaderListener
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
@Slf4j
public class KioskCashierContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext wac = initWebApplicationContext(event.getServletContext());
        wac.publishEvent(new ApplicationStartEvent(this));
    }
}
