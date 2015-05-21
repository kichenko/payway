/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.bus;

/**
 * UIEventBus
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public interface UIEventBus {

    void addSubscriber(Object subscriber);

    void removeSubscriber(Object subscriber);

    void sendNotification(Object event);
}
