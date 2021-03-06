/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.core.service.bean.BeanService;
import com.payway.advertising.core.service.config.apply.ConfigurationApplyService;
import com.payway.advertising.ui.component.notification.apply.ApplyConfigurationNotificationItemView;
import com.payway.advertising.ui.component.notification.events.apply.ApplyConfigurationNotificationEvent;
import com.payway.advertising.ui.component.notification.events.common.NotificationEvent;
import com.payway.advertising.ui.component.notification.events.report.ExecuteReportNotificationEvent;
import com.payway.advertising.ui.component.notification.report.ExecuteReportNotificationItemView;
import com.vaadin.data.util.BeanItem;
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
            private static final long serialVersionUID = 5019806363620874205L;

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
                private static final long serialVersionUID = 751019052034176230L;

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
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {

                VerticalLayout layout = new VerticalLayout();
                NotificationEvent bean = ((BeanItemContainer<NotificationEvent>) wndNotifications.getGridNotifications().getContainerDataSource()).getItem(itemId).getBean();

                layout.setSizeFull();
                if (bean instanceof ApplyConfigurationNotificationEvent) {
                    ApplyConfigurationNotificationEvent event = (ApplyConfigurationNotificationEvent) bean;
                    layout.addComponent(
                            new ApplyConfigurationNotificationItemView(
                                    "Apply configuration",
                                    event.getStatus(),
                                    event.getUserName(),
                                    event.getDateCreate(),
                                    event.getDateStatus(),
                                    new NotificationItemAction() {
                                        @Override
                                        public void close() {
                                            wndNotifications.getGridNotifications().removeItem(itemId);
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
                } else if (bean instanceof ExecuteReportNotificationEvent) {
                    ExecuteReportNotificationEvent event = (ExecuteReportNotificationEvent) bean;
                    layout.addComponent(
                            new ExecuteReportNotificationItemView(
                                    event.getName(),
                                    event.getStatus(),
                                    event.getUserName(),
                                    event.getStart(),
                                    event.getStatusDate(),
                                    new NotificationItemAction() {
                                        @Override
                                        public void close() {
                                            wndNotifications.getGridNotifications().removeItem(itemId);
                                        }

                                        @Override
                                        public boolean cancel() {
                                            return false;
                                        }

                                        @Override
                                        public void click() {
                                            //
                                        }
                                    },
                                    event.getArgs()
                            ));
                }

                return layout;
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

    private void processNotifications(NotificationEvent event) {

        BeanItemContainer<NotificationEvent> container = (BeanItemContainer<NotificationEvent>) wndNotifications.getGridNotifications().getContainerDataSource();
        BeanItem<NotificationEvent> item = container.getItem(event);

        if (item != null) {

            if (event instanceof ExecuteReportNotificationEvent) {

                ExecuteReportNotificationEvent ev = (ExecuteReportNotificationEvent) event;
                ExecuteReportNotificationEvent bean = (ExecuteReportNotificationEvent) item.getBean();

                bean.setArgs(ev.getArgs());
                bean.setStatus(ev.getStatus());
                bean.setStatusDate(ev.getStatusDate());

            } else if (event instanceof ApplyConfigurationNotificationEvent) {

                ApplyConfigurationNotificationEvent ev = (ApplyConfigurationNotificationEvent) event;
                ApplyConfigurationNotificationEvent bean = (ApplyConfigurationNotificationEvent) item.getBean();

                bean.setArgs(ev.getArgs());
                bean.setStatus(ev.getStatus());
                bean.setDateStatus(ev.getDateStatus());
            } else {
                log.error("Invalid event type, cannot process [{}]", event);
                return;
            }
            wndNotifications.getGridNotifications().refreshRowCache();
        } else {
            if (container.addItem(event) != null) {
                countUnread += 1;
            } else {
                log.error("Cannot add item notification");
            }
        }
    }

    @Subscribe
    public void processNotificationEvent(final NotificationEvent event) {

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                processNotifications(event);
                refreshUnreadCount();
                UI.getCurrent().push();
            }
        });
    }
}
