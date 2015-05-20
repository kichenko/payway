/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

/**
 * NotificationsButton
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
public class NotificationsButton extends Button {

    private static final String STYLE_BUTTON_NOTIFICATIONS = "btn-notifications";
    private static final String STYLE_WINDOW_NOTIFICATIONS = "btn-notifications-window";
    private static final String STYLE_BUTTON_NOTIFICATIONS_UNREAD = "btn-notifications-unread";

    private Window wndNotifications;

    public NotificationsButton() {
        setId("btnNotifications");
        addStyleName(STYLE_BUTTON_NOTIFICATIONS);
        setIcon(new ThemeResource("images/components/btn_notification.png"));
        //addStyleName(ValoTheme.BUTTON_ICON_ONLY);

        this.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                openNotificationsPopup(event);
            }
        });
    }

    public void setUnreadCount(final int count) {
        setCaption(String.valueOf(count));
        if (count > 0) {
            addStyleName(STYLE_WINDOW_NOTIFICATIONS);
        } else {
            removeStyleName(STYLE_BUTTON_NOTIFICATIONS_UNREAD);
        }
    }

    public void openNotificationsPopup(final ClickEvent event) {
        
        if (wndNotifications == null) {
            
            wndNotifications = new Window();
            wndNotifications.setWidth(300.0f, Unit.PIXELS);
            wndNotifications.addStyleName("window-btn-notifications");
            wndNotifications.setClosable(false);
            wndNotifications.setResizable(false);
            wndNotifications.setDraggable(false);
            wndNotifications.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);
            //wndNotifications.setContent(notificationsLayout);
        }

        if (!wndNotifications.isAttached()) {
            wndNotifications.setPositionY(event.getClientY() - event.getRelativeY() + 40);
            getUI().addWindow(wndNotifications);
            wndNotifications.focus();
        } else {
            wndNotifications.close();
        }
    }
}
