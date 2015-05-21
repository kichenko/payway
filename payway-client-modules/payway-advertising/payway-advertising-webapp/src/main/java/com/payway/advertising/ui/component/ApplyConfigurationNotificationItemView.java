/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.payway.advertising.core.service.config.apply.ApplyConfigurationStatus;
import com.payway.advertising.core.service.notification.NotificationEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * ApplyConfigurationNotificationItemView
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
public class ApplyConfigurationNotificationItemView extends AbstractNotificationItemView {

    @UiField
    private Label lblCaption;

    @UiField
    private Button btnClose;

    @UiField
    private Label lblStatus;

    @UiField
    private Label lblLogin;

    @UiField
    private Label lblDate;

    @UiField
    private Button btnShow;

    private NotificationItemAction action;

    private void init() {
        setSizeFull();
        addComponent(Clara.create("ApplyConfigurationNotificationItemView.xml", this));
    }

    public ApplyConfigurationNotificationItemView(String caption, NotificationEvent.EventType kind, ApplyConfigurationStatus.Step step, String login, LocalDateTime dateCreate, NotificationItemAction action) {

        this.action = action;
        init();

        lblCaption.setValue(caption);
        lblStatus.setValue(step.toString());
        lblLogin.setValue(login);
        lblDate.setValue(dateCreate.toString("dd.MM.yyyy HH:mm:ss"));

        if (ApplyConfigurationStatus.Step.Fail.equals(step) || ApplyConfigurationStatus.Step.Success.equals(step) || ApplyConfigurationStatus.Step.Cancel.equals(step)) {
            btnClose.setEnabled(true);
        } else {
            btnClose.setEnabled(false);
        }

        btnClose.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (ApplyConfigurationNotificationItemView.this.action != null) {
                    ApplyConfigurationNotificationItemView.this.action.close();
                }
            }
        });

        btnShow.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (ApplyConfigurationNotificationItemView.this.action != null) {
                    ApplyConfigurationNotificationItemView.this.action.click();
                }
            }
        });
    }
}
