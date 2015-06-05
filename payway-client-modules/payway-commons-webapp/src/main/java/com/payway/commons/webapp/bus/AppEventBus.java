/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.bus;

/**
 * AbstractNotificationItem
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public interface AppEventBus {

    void addSubscriber(Object subscriber);

    void removeSubscriber(Object subscriber);

}
