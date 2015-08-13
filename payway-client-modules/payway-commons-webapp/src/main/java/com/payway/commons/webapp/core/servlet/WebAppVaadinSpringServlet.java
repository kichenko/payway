/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.core.servlet;

import com.payway.commons.webapp.bus.AppEventPublisher;
import com.payway.commons.webapp.bus.EventBusBridge;
import com.payway.commons.webapp.bus.event.SessionCreatedAppEventBus;
import com.payway.commons.webapp.bus.event.SessionDestroyedAppEventBus;
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
public class WebAppVaadinSpringServlet extends SpringVaadinServlet {

    public static final String WEB_APP_SESSION_ID_ATTRIBUTE_NAME = "web-app-session-id";

    private static final long serialVersionUID = -3811629641190246104L;

    @Override
    protected void servletInitialized() throws ServletException {

        super.servletInitialized();

        getService().addSessionInitListener(new SessionInitListener() {
            private static final long serialVersionUID = 7085149115599486944L;

            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {

                event.getSession().setAttribute(WEB_APP_SESSION_ID_ATTRIBUTE_NAME, event.getSession().getSession().getId());

                WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                if (wac != null) {
                    EventBusBridge eventBusBridge = (EventBusBridge) wac.getBean("app.EventBusBridge");
                    if (eventBusBridge != null) {
                        eventBusBridge.addSession(event.getSession());
                        AppEventPublisher appEventPublisher = (AppEventPublisher) wac.getBean("app.AppEventBus");
                        if (appEventPublisher != null) {
                            appEventPublisher.sendNotification(new SessionCreatedAppEventBus(event.getSession().getSession().getId()));
                        }
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
                    EventBusBridge eventBusBridge = (EventBusBridge) wac.getBean("app.EventBusBridge");
                    if (eventBusBridge != null) {
                        AppEventPublisher appEventPublisher = (AppEventPublisher) wac.getBean("app.AppEventBus");
                        if (appEventPublisher != null) {
                            appEventPublisher.sendNotification(new SessionDestroyedAppEventBus((String) event.getSession().getAttribute(WEB_APP_SESSION_ID_ATTRIBUTE_NAME)));
                        }
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
