/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.core.service.bean.BeanService;
import com.payway.advertising.core.service.config.apply.ConfigurationApplyService;
import com.payway.advertising.ui.component.ApplyConfigurationNotificationItemView;
import com.payway.advertising.ui.component.NotificationItemAction;
import com.payway.advertising.ui.component.notification.events.ApplyConfigurationNotificationEvent;
import com.payway.advertising.ui.component.notification.events.NotificationEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lombok.Getter;
import lombok.Setter;
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
    private static final String STYLE_BUTTON_NOTIFICATIONS_UNREAD = "btn-notifications-unread";

    private int countUnread;
    private NotificationsButtonPopupWindow wndNotifications;

    @Getter
    @Setter
    private boolean popup;

    @Setter
    @Getter
    private BeanService beanService;

    public NotificationsButton() {
        init();
    }

    private void init() {

        setIcon(new ThemeResource("images/components/button_notifications/btn_notifications.png"));
        initWindowNotifications();
        initGridNotifications();

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
            wndNotifications = new NotificationsButtonPopupWindow();
            wndNotifications.addCloseListener(new Window.CloseListener() {
                @Override
                public void windowClose(Window.CloseEvent e) {
                    setPopup(false);
                }
            });
        }
    }

    private void initGridNotifications() {

        wndNotifications.getGridNotifications().setContainerDataSource(new BeanItemContainer<>(NotificationEvent.class));
        wndNotifications.getGridNotifications().addGeneratedColumn("notification", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                final Object selectedItemId = itemId;
                NotificationEvent bean = ((BeanItemContainer<NotificationEvent>) wndNotifications.getGridNotifications().getContainerDataSource()).getItem(itemId).getBean();
                if (bean != null) {
                    if (bean instanceof ApplyConfigurationNotificationEvent) {
                        ApplyConfigurationNotificationEvent event = (ApplyConfigurationNotificationEvent) bean;

                        VerticalLayout layout = new VerticalLayout();
                        layout.setSizeFull();
                        layout.addComponent(
                          new ApplyConfigurationNotificationItemView("Apply configuration",
                            event.getStatus(),
                            event.getUserName(),
                            event.getDateCreate(),
                            event.getDateStatus(),
                            new NotificationItemAction() {
                                @Override
                                public void close() {
                                    wndNotifications.getGridNotifications().removeItem(selectedItemId);
                                }

                                @Override
                                public boolean cancel() {
                                    ConfigurationApplyService service = (ConfigurationApplyService) beanService.getBean("app.advertising.ConfigurationApplyService");
                                    if (service != null) {
                                        return service.cancel();
                                    } else {
                                        log.error("Error get configuration apply service");
                                    }

                                    return false;
                                }

                                @Override
                                public void click() {
                                    //
                                }
                            },
                            event.getArgs()
                          ));

                        return layout;
                    }
                }
                return "";
            }
        });

        wndNotifications.getGridNotifications().setVisibleColumns("notification");
    }

    public void refreshUnreadCount() {

        setCaption(countUnread <= 0 ? null : Integer.toString(countUnread));
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
            setPopup(true);
        } else {
            wndNotifications.close();
            setPopup(false);
        }
    }

    private void processAppyConfigurationNotification(ApplyConfigurationNotificationEvent event) {

        BeanItemContainer<ApplyConfigurationNotificationEvent> container = (BeanItemContainer<ApplyConfigurationNotificationEvent>) wndNotifications.getGridNotifications().getContainerDataSource();
        if (container != null) {
            if (container.getItem(event) != null) {
                ApplyConfigurationNotificationEvent bean = container.getItem(event).getBean();
                bean.setUserName(event.getUserName());
                bean.setStatus(event.getStatus());
                bean.setDateCreate(event.getDateCreate());
                bean.setDateStatus(event.getDateStatus());
                bean.setArgs(event.getArgs());

                wndNotifications.getGridNotifications().refreshRowCache();
            } else {
                if (container.addItem(new ApplyConfigurationNotificationEvent(event.getUserName(), event.getDateCreate(), event.getStatus(), event.getDateStatus(), event.getArgs())) != null) {
                    if (countUnread == 0) {
                        countUnread = 1;
                    }
                } else {
                    log.error("can not add item notification");
                }
            }
        }
    }

    @Subscribe
    public void processNotificationEvent(final NotificationEvent event) {

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {

                if (event instanceof ApplyConfigurationNotificationEvent) {
                    processAppyConfigurationNotification((ApplyConfigurationNotificationEvent) event);
                } else {
                    log.error("Error invalid event type, can not process", event);
                }

                refreshUnreadCount();
                UI.getCurrent().push();
            }
        });
    }
}
