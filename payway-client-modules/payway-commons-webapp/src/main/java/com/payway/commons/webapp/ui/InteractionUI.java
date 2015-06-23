/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui;

import com.vaadin.ui.Notification;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;

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

    MessageBox showMessageBox(String title, String message, Icon icon, MessageBoxListener listener, ButtonId... buttonIds);
}
