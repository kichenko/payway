/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import lombok.Getter;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * NotificationsButtonPopupWindow
 *
 * @author Sergey Kichenko
 * @created 22.05.15 00:00
 */
public class NotificationsButtonPopupWindow extends Window {

    private static final long serialVersionUID = 1817415202432008138L;

    private static final String STYLE_WINDOW_NOTIFICATIONS = "btn-notifications-window";

    @UiField
    @Getter
    private Table gridNotifications;

    public NotificationsButtonPopupWindow() {
        init();
    }

    private void init() {
        setClosable(false);
        setResizable(false);
        setDraggable(false);
        addStyleName(STYLE_WINDOW_NOTIFICATIONS);
        setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
        setContent(Clara.create("NotificationsButtonPopupWindow.xml", this));

        gridNotifications.setSelectable(false);
    }
}
