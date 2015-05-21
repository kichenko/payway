/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.core.service.notification.ApplyConfigurationNotificationEvent;
import com.payway.advertising.core.service.notification.NotificationEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lombok.extern.slf4j.Slf4j;

/**
 * NotificationsButton
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
@Slf4j
public class NotificationsButton extends Button {

    private static final long serialVersionUID = -6450805530961299713L;

    private static final String BUTTON_NOTIFICATIONS_ID = "btn-notifications";
    private static final String STYLE_BUTTON_NOTIFICATIONS = "btn-notifications";
    private static final String STYLE_WINDOW_NOTIFICATIONS = "btn-notifications-window";
    private static final String STYLE_BUTTON_NOTIFICATIONS_UNREAD = "btn-notifications-unread";

    private int countUnread;
    private Window wndNotifications;
    private Table gridNotifications;

    public NotificationsButton() {
        init();
    }

    private void init() {

        setId(BUTTON_NOTIFICATIONS_ID);
        addStyleName(STYLE_BUTTON_NOTIFICATIONS);
        setIcon(new ThemeResource("images/components/btn_notifications/btn_notifications.png"));
        //addStyleName(ValoTheme.BUTTON_ICON_ONLY);

        initWindowNotifications();

        this.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                NotificationsButton.this.countUnread = 0;
                refreshUnreadCount();
                showPopup(event);
            }
        });
    }

    private void initWindowNotifications() {

        if (wndNotifications == null) {
            wndNotifications = new Window();
            wndNotifications.setWidth(300.0f, Unit.PIXELS);
            wndNotifications.addStyleName(STYLE_WINDOW_NOTIFICATIONS);
            wndNotifications.setClosable(false);
            wndNotifications.setResizable(false);
            wndNotifications.setDraggable(false);
            wndNotifications.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE, null);

            if (gridNotifications == null) {
                initGridNotifications();
            }

            VerticalLayout layout = new VerticalLayout();
            layout.setSizeFull();
            layout.addComponent(gridNotifications);

            wndNotifications.setContent(layout);
        }
    }

    private void initGridNotifications() {

        gridNotifications = new Table();
        gridNotifications.setSizeFull();
        gridNotifications.setSelectable(true);
        gridNotifications.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        gridNotifications.setContainerDataSource(new BeanItemContainer<>(NotificationEvent.class));

        gridNotifications.addGeneratedColumn("notification", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {

                final Object selectedItemId = itemId;

                NotificationEvent bean = ((BeanItemContainer<NotificationEvent>) gridNotifications.getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {
                    if (bean instanceof ApplyConfigurationNotificationEvent) {
                        ApplyConfigurationNotificationEvent event = (ApplyConfigurationNotificationEvent) bean;

                        VerticalLayout layout = new VerticalLayout();
                        layout.setSizeFull();
                        layout.addComponent(new ApplyConfigurationNotificationItemView("Apply configuration",
                                event.getKind(),
                                event.getStep(),
                                event.getUserName(),
                                event.getDateCreate(),
                                new NotificationItemAction() {
                                    @Override
                                    public void close() {
                                        gridNotifications.removeItem(selectedItemId);
                                    }

                                    @Override
                                    public void click() {
                                        //как показать окно прогресс бара применения конфигурации????
                                    }
                                }
                        ));

                        ConfigurationApplyWindow wnd = new ConfigurationApplyWindow();
                        wnd.refresh(null);
                        wnd.show();

                        return layout;
                    }
                }
                return "";
            }
        });

        gridNotifications.setVisibleColumns("notification");
    }

    public void refreshUnreadCount() {

        setCaption(Integer.toString(countUnread));
        if (countUnread > 0) {
            addStyleName(STYLE_BUTTON_NOTIFICATIONS_UNREAD);
        } else {
            removeStyleName(STYLE_BUTTON_NOTIFICATIONS_UNREAD);
        }
    }

    public void showPopup(final ClickEvent event) {

        if (!wndNotifications.isAttached()) {
            wndNotifications.setPositionY(event.getClientY() - event.getRelativeY() + 40);
            getUI().addWindow(wndNotifications);
            wndNotifications.focus();
        } else {
            wndNotifications.close();
        }
    }

    private void processAppyConfigurationNotification(ApplyConfigurationNotificationEvent event) {

        BeanItemContainer<ApplyConfigurationNotificationEvent> container = (BeanItemContainer<ApplyConfigurationNotificationEvent>) gridNotifications.getContainerDataSource();
        if (container != null) {
            if (container.getItem(event) != null) {
                ApplyConfigurationNotificationEvent bean = container.getItem(event).getBean();

                bean.setUserName(event.getUserName());
                bean.setStep(event.getStep());
                bean.setDateCreate(event.getDateCreate());

                gridNotifications.refreshRowCache();
            } else {
                if (NotificationEvent.EventType.Fixed.equals(event.getKind())) {
                    if (container.size() > 0) { //add on top 
                        container.addItemAt(0, event);
                    } else {
                        container.addItem(event);
                    }
                } else {
                    container.addItem(event);
                }
            }
        }
    }

    @Subscribe
    public void processNotificationEvent(NotificationEvent event) {

        countUnread = countUnread + 1;
        refreshUnreadCount();
        if (event instanceof ApplyConfigurationNotificationEvent) {
            processAppyConfigurationNotification((ApplyConfigurationNotificationEvent) event);
        } else {
            log.error("Error invalid event type, cannot process it", event);
        }
    }
}
