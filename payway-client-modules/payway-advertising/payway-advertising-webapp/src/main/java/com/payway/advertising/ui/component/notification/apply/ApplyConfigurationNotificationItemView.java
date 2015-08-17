/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification.apply;

import com.payway.advertising.core.service.config.apply.ApplyStatus;
import com.payway.advertising.ui.component.notification.AbstractNotificationItemView;
import com.payway.advertising.ui.component.notification.NotificationItemAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * ApplyConfigurationNotificationItemView
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
public class ApplyConfigurationNotificationItemView extends AbstractNotificationItemView {

    private static final long serialVersionUID = -6217297030176205599L;

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
    private ProgressBar progressBar;

    @UiField
    private Label lblProgress;

    @UiField
    private Button btnCancel;

    @Setter
    @Getter
    private NotificationItemAction action;

    public ApplyConfigurationNotificationItemView(String caption, ApplyStatus status, String login, LocalDateTime dateCreate, LocalDateTime dateStatus, NotificationItemAction action, Object... args) {

        init();
        setAction(action);
        refresh(caption, login, status, dateCreate, dateStatus, args);
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("ApplyConfigurationNotificationItemView.xml", this));
    }

    private void refresh(String caption, String login, ApplyStatus status, LocalDateTime dateCreate, LocalDateTime dateStatus, Object... args) {

        lblLogin.setValue(login);
        lblCaption.setValue(caption);

        if (ApplyStatus.Fail.equals(status) || ApplyStatus.Success.equals(status) || ApplyStatus.Cancel.equals(status)) {
            btnClose.setEnabled(true);
        } else {
            btnClose.setEnabled(false);
        }

        if (dateCreate != null) {
            lblStartDate.setValue(dateCreate.toString("dd.MM.yyyy HH:mm"));
        } else {
            lblStartDate.setValue("");
        }

        if (dateStatus != null) {
            lblStatusDate.setValue(dateStatus.toString("dd.MM.yyyy HH:mm"));
        } else {
            lblStatusDate.setValue("");
        }

        switch (status) {

            case Prepare: {
                lblStatus.setValue("Preparing...");
                lblProgress.setValue(null);
                progressBar.setValue(new Float(0.1));
                btnCancel.setEnabled(true);
            }
            break;

            case CopyFiles: {
                lblStatus.setValue("Copying files...");
                if (args != null && args.length == 3) {
                    lblProgress.setValue(String.format("Files: [%d/%d], %s", args));
                    progressBar.setValue(new Float(0.6));
                    btnCancel.setEnabled(true);
                }
            }
            break;

            case UpdateDatabase: {
                lblStatus.setValue("Updating database...");
                lblProgress.setValue(null);
                progressBar.setValue(new Float(0.7));
                btnCancel.setEnabled(false);
            }
            break;

            case Confirmation: {
                lblStatus.setValue("Confirmation...");
                lblProgress.setValue(null);
                progressBar.setValue(new Float(0.9));
                btnCancel.setEnabled(false);
            }
            break;

            case Success: {
                lblStatus.setValue("Success");
                lblProgress.setValue(null);
                progressBar.setValue(new Float(1));
                btnCancel.setEnabled(false);
            }
            break;

            case Canceling: {
                lblStatus.setValue("Canceling...");
                progressBar.setValue(new Float(0));
                lblProgress.setValue(null);
                btnCancel.setEnabled(false);
            }
            break;

            case Cancel: {
                lblStatus.setValue("Canceled");
                progressBar.setValue(new Float(0));
                lblProgress.setValue(null);
                btnCancel.setEnabled(false);
            }
            break;

            case Fail: {
                lblStatus.setValue("Failed");
                progressBar.setValue(new Float(0));
                lblProgress.setValue(null);
                btnCancel.setEnabled(false);
            }
            break;
        }
    }

    @UiHandler(value = "btnClose")
    public void onClickClose(Button.ClickEvent event) {
        if (getAction() != null) {
            getAction().close();
        }
    }

    @UiHandler(value = "btnCancel")
    public void onClickCancel(Button.ClickEvent event) {
        if (getAction() != null) {
            btnCancel.setEnabled(!getAction().cancel());
        }
    }
}
