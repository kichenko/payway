/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.payway.advertising.core.service.config.apply.ApplyStatus;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
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
    private Label lblStatusDate;

    @UiField
    private Label lblLogin;

    @UiField
    private Label lblStartDate;

    @UiField
    private Button btnShow;

    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private NotificationItemAction action;

    private void init() {
        setSizeFull();
        addComponent(Clara.create("ApplyConfigurationNotificationItemView.xml", this));
    }

    public ApplyConfigurationNotificationItemView(String caption, ApplyStatus status, String login, LocalDateTime dateCreate, LocalDateTime dateStatus, NotificationItemAction action) {

        init();
        setAction(action);

        lblCaption.setValue(caption);
        lblStatus.setValue(status.toString());
        lblLogin.setValue(login);

        if (dateCreate != null) {
            lblStartDate.setValue(dateCreate.toString("dd.MM.yyyy HH:mm:ss"));
        } else {
            lblStartDate.setValue("");
        }

        if (dateStatus != null) {
            lblStatusDate.setValue(dateStatus.toString("dd.MM.yyyy HH:mm:ss"));
        } else {
            lblStatusDate.setValue("");
        }

        if (ApplyStatus.Fail.equals(status) || ApplyStatus.Success.equals(status) || ApplyStatus.Cancel.equals(status)) {
            btnClose.setEnabled(true);
        } else {
            btnClose.setEnabled(false);
        }

        btnClose.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (ApplyConfigurationNotificationItemView.this.getAction() != null) {
                    ApplyConfigurationNotificationItemView.this.getAction().close();
                }
            }
        });

        btnShow.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (ApplyConfigurationNotificationItemView.this.getAction() != null) {
                    ApplyConfigurationNotificationItemView.this.getAction().click();
                }
            }
        });
    }
}
