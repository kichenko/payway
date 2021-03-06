/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.config.SubscribeOnAppEventBus;
import com.payway.commons.webapp.ui.AbstractUI;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * EventBusBridgeImpl
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
@SubscribeOnAppEventBus
@Component(value = "app.EventBusBridge")
public class EventBusBridgeImpl implements EventBusBridge {

    private final Set<VaadinSession> sessions = Sets.newConcurrentHashSet();

    @Override
    public void addSession(VaadinSession session) {

        if (log.isDebugEnabled()) {
            log.debug("Add session {}", session);
        }
        sessions.add(session);
    }

    @Override
    public void removeSession(VaadinSession session) {

        if (log.isDebugEnabled()) {
            log.debug("Remove session {}", session);
        }
        sessions.remove(session);
    }

    @Subscribe
    public void processEvent(Object event) {

        if (log.isDebugEnabled()) {
            log.debug("Process event {}", event);
        }

        try {
            for (VaadinSession s : sessions) {
                for (UI ui : s.getUIs()) {
                    if (ui instanceof AbstractUI) {
                        UI currentUI = UI.getCurrent();
                        try {
                            UI.setCurrent(ui);
                            ((AbstractUI) ui).getSessionEventBus().sendNotification(event);
                        } catch (Exception ex) {
                            throw ex;
                        } finally {
                            UI.setCurrent(currentUI);
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Processed UI is not instance of class {}", ui.getClass());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error process app bus event", ex);
        }
    }
}
