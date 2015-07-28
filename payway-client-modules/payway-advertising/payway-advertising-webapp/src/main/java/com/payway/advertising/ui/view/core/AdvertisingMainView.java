/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.core.service.bean.BeanService;
import com.payway.advertising.ui.bus.events.CloseNotificationsButtonPopupWindowsEvent;
import com.payway.advertising.ui.component.FileUploadPanel;
import com.payway.advertising.ui.component.UploadButtonWrapper;
import com.payway.advertising.ui.component.UploadTaskPanel;
import com.payway.advertising.ui.component.notification.NotificationsButton;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.vaadin.event.LayoutEvents;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalSplitPanel;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AdvertisingMainView
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@UIScope
@NoArgsConstructor
@Component(value = "app.advertising.AdvertisingMainView")
public class AdvertisingMainView extends AbstractMainView {

    private static final long serialVersionUID = -4825092972126420478L;

    public static final float SIDEBAR_DEFAULT_WIDTH_PERCENT = 20;
    public static final float UPLOADS_DEFAULT_HEIGHT_PERCENT = 50;
    public static final float SIDEBAR_DEFAULT_HEIGHT_PERCENT = 50;

    @Autowired
    private BeanService beanService;

    @UiField
    private UploadButtonWrapper btnFileUploadToolBar;

    @UiField
    @Getter
    private NotificationsButton btnNotifications;

    @UiField
    private UploadTaskPanel uploadTaskPanel;

    @UiField
    private FileUploadPanel fileUploadPanel;

    @UiField
    private VerticalSplitPanel splitVerticalPanel;

    @UiField
    private VerticalSplitPanel splitVerticalPanelUploads;

    @UiField
    private CssLayout layoutUploads;

    @UiField
    private CssLayout layoutSideBar;

    @UiField
    private CssLayout layoutUploadTasks;

    @UiField
    private CssLayout layoutFileUpload;

    @PostConstruct
    public void init() {
        setSizeFull();
        setCompositionRoot(Clara.create("AdvertisingMainView.xml", this));

        splitHorizontalPanel.setFirstComponent(layoutLeft);
        splitHorizontalPanel.setSecondComponent(layoutRight);
        splitHorizontalPanel.setSplitPosition(SIDEBAR_DEFAULT_WIDTH_PERCENT, Unit.PERCENTAGE);

        splitVerticalPanel.setFirstComponent(layoutSideBar);
        splitVerticalPanel.setSecondComponent(layoutUploads);
        splitVerticalPanel.setSplitPosition(SIDEBAR_DEFAULT_HEIGHT_PERCENT, Unit.PERCENTAGE);

        splitVerticalPanelUploads.setFirstComponent(layoutUploadTasks);
        splitVerticalPanelUploads.setSecondComponent(layoutFileUpload);
        splitVerticalPanelUploads.setSplitPosition(UPLOADS_DEFAULT_HEIGHT_PERCENT, Unit.PERCENTAGE);

        mainViewLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private static final long serialVersionUID = -6131089921825563746L;

            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                if (btnNotifications.isPopup()) {
                    AbstractUI ui = (AbstractUI) getUI();
                    if (ui != null) {
                        ui.getSessionEventBus().sendNotification(new CloseNotificationsButtonPopupWindowsEvent());
                    }
                }
            }
        });

        btnNotifications.setBeanService(beanService);
    }

    @Override
    public void initialize() {
        //
    }

    @Override
    public void setUpCustomWorkspaceView(WorkspaceView workspaceView) {
        AbstractAdvertisingWorkspaceView v = (AbstractAdvertisingWorkspaceView) workspaceView;
        if (v != null) {
            v.setButtonFileUploadToolBar(btnFileUploadToolBar);
            v.setUploadTaskPanel(uploadTaskPanel);
            v.setFileUploadPanel(fileUploadPanel);
        }
    }
}
