/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.core.servlet;

import com.payway.commons.webapp.bus.EventBusBridge;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.spring.server.SpringVaadinServlet;
import javax.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet for support vaadin & spring
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Slf4j
public class PaywayCommonVaadinSpringServlet extends SpringVaadinServlet {

    private static final long serialVersionUID = -3811629641190246104L;

    @Override
    protected void servletInitialized() throws ServletException {

        super.servletInitialized();

        getService().addSessionInitListener(new SessionInitListener() {
            private static final long serialVersionUID = 7085149115599486944L;

            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {

                WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                if (wac != null) {
                    EventBusBridge eventBusBridge = (EventBusBridge) wac.getBean("eventBusBridge");
                    if (eventBusBridge != null) {
                        eventBusBridge.addSession(event.getSession());
                    } else {
                        log.error("Error can not get event bus bridge from web application context");
                    }
                } else {
                    log.error("Error can not get web application context");
                }
            }
        });

        getService().addSessionDestroyListener(new SessionDestroyListener() {
            private static final long serialVersionUID = 7311256375749740121L;

            @Override
            public void sessionDestroy(SessionDestroyEvent event) {

                WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                if (wac != null) {
                    EventBusBridge eventBusBridge = (EventBusBridge) wac.getBean("eventBusBridge");
                    if (eventBusBridge != null) {
                        eventBusBridge.removeSession(event.getSession());
                    } else {
                        log.error("Error can not get event bus bridge from web application context");
                    }
                } else {
                    log.error("Error can not get web application context");
                }
            }
        });
    }
}
