/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.bus;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.ui.AbstractUI;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final Set<VaadinSession> sessions = new HashSet<>(); //?

    @Autowired
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

        try {
            for (VaadinSession s : sessions) {
                for (UI ui : s.getUIs()) {
                    if (ui instanceof AbstractUI) {
                        ((AbstractUI) ui).getEventBus().sendNotification(event.getData());
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("processed ui is not instanceof AbstractUI {}", ui);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error process event", ex);
        }
    }
}
