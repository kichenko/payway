/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

/**
 * NotificationItemAction
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public interface NotificationItemAction {

    void close();

    void click();

    boolean cancel();
}
