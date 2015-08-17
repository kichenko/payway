/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component.notification.report;

import com.payway.advertising.ui.component.notification.AbstractNotificationItemView;
import com.payway.advertising.ui.component.notification.NotificationItemAction;
import com.payway.advertising.ui.component.notification.events.report.ExecuteReportStatusType;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * ExecuteReportNotificationItemView
 *
 * @author Sergey Kichenko
 * @created 14.08.2015
 */
public class ExecuteReportNotificationItemView extends AbstractNotificationItemView {

    private static final long serialVersionUID = 6400031561642427680L;

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
    private Button btnOpen;

    @Setter
    @Getter
    private NotificationItemAction action;

    @Setter
    @Getter
    private String reportURL;

    public ExecuteReportNotificationItemView(String caption, ExecuteReportStatusType status, String login, LocalDateTime dateCreate, LocalDateTime dateStatus, NotificationItemAction action, Object... args) {

        init();
        setAction(action);
        refresh(caption, login, status, dateCreate, dateStatus, args);
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("ExecuteReportNotificationItemView.xml", this));
    }

    private void refresh(String caption, String login, ExecuteReportStatusType status, LocalDateTime dateCreate, LocalDateTime dateStatus, Object... args) {

        lblLogin.setValue(login);
        lblCaption.setValue(caption);

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

            case Run: {
                lblStatus.setValue("Executing");
                lblProgress.setValue("Executing, please wait...");
                progressBar.setValue(new Float(0.5));

                btnOpen.setEnabled(false);
                btnClose.setEnabled(false);
            }
            break;

            case Success: {
                lblStatus.setValue("Success");
                lblProgress.setValue("Executing successfully");
                progressBar.setValue(new Float(1));

                if (args != null && args.length > 0 && args[0] instanceof String) {
                    setReportURL((String) args[0]);
                }

                btnOpen.setEnabled(true);
                btnClose.setEnabled(true);
            }
            break;

            case Fail: {
                lblStatus.setValue("Failed");
                progressBar.setValue(new Float(0));
                lblProgress.setValue("Failed executing report");

                btnOpen.setEnabled(false);
                btnClose.setEnabled(true);

                if (args != null && args.length > 0 && args[0] instanceof Exception) {
                    lblStatus.setDescription(((Exception) args[0]).getMessage());
                }
            }
            break;
        }
    }

    @UiHandler(value = "btnOpen")
    public void onClickOpen(Button.ClickEvent event) {
        Page.getCurrent().open(getReportURL(), "_blank");
    }

    @UiHandler(value = "btnClose")
    public void onClickClose(Button.ClickEvent event) {
        if (getAction() != null) {
            getAction().close();
        }
    }
}
