/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.app.bus;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.payway.advertising.ui.AbstractUI;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * EventBusBridgeImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
@Component(value = "eventBusBridge")
public class EventBusBridgeImpl implements EventBusBridge {

    private final Set<VaadinSession> sessions = Sets.newConcurrentHashSet();

    @Autowired
    @Qualifier(value = "appEventBus")
    private AppEventBus appEventBus;

    @PostConstruct
    public void postConstruct() {
        appEventBus.addSubscriber(this);
    }

    @PreDestroy
    public void preDestroy() {
        appEventBus.removeSubscriber(this);
    }

    @Override
    public void addSession(VaadinSession session) {

        if (log.isDebugEnabled()) {
            log.debug("add session {}", session);
        }
        sessions.add(session);
    }

    @Override
    public void removeSession(VaadinSession session) {

        if (log.isDebugEnabled()) {
            log.debug("remove session {}", session);
        }
        sessions.remove(session);
    }

    @Subscribe
    public void processEvent(AppBusEvent event) {

        if (log.isDebugEnabled()) {
            log.debug("process event {}", event);
        }

        if (log.isDebugEnabled()) {
            log.debug("Debug event bridge bus events");
            log.debug("Current UI - {} ", UI.getCurrent());
            log.debug("Current thread - {}", Thread.currentThread());
        }

        try {
            for (VaadinSession s : sessions) {
                for (UI ui : s.getUIs()) {
                    if (ui instanceof AbstractUI) {
                        UI currentUI = UI.getCurrent();
                        try {
                            UI.setCurrent(ui);
                            ((AbstractUI) ui).getSessionEventBus().sendNotification(event.getData());
                        } catch (Exception ex) {
                            throw ex;
                        } finally {
                            UI.setCurrent(currentUI);
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Processed UI is not instance of AbstractUI class {}", ui);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error process app bus event", ex);
        }
    }
}
