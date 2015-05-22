/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.bus;

/**
 * SessionEventBus
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public interface SessionEventBus {

    void addSubscriber(Object subscriber);

    void removeSubscriber(Object subscriber);

    void sendNotification(Object event);
}
