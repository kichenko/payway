/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.bus;

import com.vaadin.server.VaadinSession;

/**
 * EventBusBridge
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public interface EventBusBridge {

    void addSession(VaadinSession session);

    void removeSession(VaadinSession session);
}
