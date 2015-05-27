/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui;

import com.payway.advertising.ui.bus.SessionEventBus;
import com.payway.advertising.ui.component.ProgressBarWindow;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

/**
 * AbstractUI
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public abstract class AbstractUI extends UI implements InteractionUI {

    public abstract SessionEventBus getSessionEventBus();

    private final ProgressBarWindow progressBarWindow = new ProgressBarWindow();

    @Override
    public void showNotification(String caption, String text, Notification.Type kind) {
        Notification.show(text, kind);
        UI.getCurrent().push();
    }

    @Override
    public void showProgressBar() {
        progressBarWindow.show();
        UI.getCurrent().push();
    }

    @Override
    public void closeProgressBar() {
        progressBarWindow.close();
        UI.getCurrent().push();
    }

}
