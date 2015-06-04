/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui;

import com.vaadin.ui.Notification;

/**
 * Notification
 *
 * @author Sergey Kichenko
 * @created 27.05.15 00:00
 */
public interface InteractionUI {

    void showNotification(String caption, String text, Notification.Type kind);

    void showProgressBar();

    void closeProgressBar();
}
