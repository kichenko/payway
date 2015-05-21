/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.google.common.eventbus.Subscribe;
import com.payway.advertising.core.service.config.apply.ApplyConfigurationStatus;
import com.payway.advertising.core.service.config.apply.ConfigurationApplyService;
import com.payway.advertising.ui.AbstractUI;
import com.payway.advertising.ui.bus.UIEventBus;
import com.payway.advertising.ui.utils.UIUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * ConfigurationApplyWindow
 *
 * @author Sergey Kichenko
 * @created 20.05.15 00:00
 */
@Slf4j
@Component(value = "configurationApplyWindow")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConfigurationApplyWindow extends Window {

    @UiField
    private Label lblDescription;

    @UiField
    private ProgressBar progressBar;

    @UiField
    private Button btnCancel;

    @Autowired
    private ConfigurationApplyService configurationApplyService;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    private void init() {

        setModal(false);
        setClosable(true);
        setDraggable(true);
        setResizable(true);
        setContent(Clara.create("ConfigurationApplyWindow.xml", this));

        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    configurationApplyService.cancel();
                } catch (Exception ex) {
                    log.error("Error cancel apply configuration", ex);
                    UIUtils.showErrorNotification("", "Error cancel apply configuration");
                }
            }
        });
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    @Override
    public void close() {
        UI.getCurrent().removeWindow(this);

        if (getUI() instanceof AbstractUI) {
            UIEventBus bus = ((AbstractUI) getUI()).getEventBus();
            if (bus != null) {
                bus.removeSubscriber(this);
            }
        }
    }

    @Subscribe
    public void onNotify(final Object event) {

        if (event instanceof ApplyConfigurationStatus) {
            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    refresh((ApplyConfigurationStatus) event);
                }
            });
        }
    }

    public void refresh(ApplyConfigurationStatus status) {
        switch (status.getStep()) {

            case Prepare: {
                lblDescription.setCaption("Prepare applying configuration");
                progressBar.setValue(new Float(0.2));
                btnCancel.setEnabled(true);
            }
            break;

            case CopyFiles: {
                int fileCount = (int) status.getArgs()[0];
                int curFileIndex = (int) status.getArgs()[1];
                String fileName = (String) status.getArgs()[2];

                lblDescription.setCaption(String.format("Copy files [%s] [%d/%d]", fileName, curFileIndex, fileCount));
                progressBar.setValue(new Float(0.4 + (0.2 / fileCount * curFileIndex)));
                btnCancel.setEnabled(true);
            }
            break;

            case UpdateDatabase: {
                lblDescription.setCaption("Update databse applying configuration");
                progressBar.setValue(new Float(0.6));
                btnCancel.setEnabled(false);
            }
            break;

            case Confirmation: {
                lblDescription.setCaption("Confirmation applying configuration");
                progressBar.setValue(new Float(0.8));
                btnCancel.setEnabled(false);
            }
            break;

            case Success: {
                lblDescription.setCaption("Success applying configuration");
                progressBar.setValue(new Float(1));
                btnCancel.setEnabled(false);
            }
            break;

            case Canceling: {
                lblDescription.setCaption("Canceling applying configuration");
                btnCancel.setEnabled(false);
            }
            break;

            case Cancel: {
                lblDescription.setCaption("Cancel applying configuration");
                btnCancel.setEnabled(false);
            }
            break;

            case Fail: {
                lblDescription.setCaption("Fail applying configuration");
                btnCancel.setEnabled(false);
            }
            break;
        }
    }
}
