/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.reporting;

import com.payway.advertising.ui.view.core.AbstractAdvertisingWorkspaceView;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.buiders.WindowBuilder;
import com.payway.webapp.reporting.ui.service.UIReportService;
import com.payway.webapp.reporting.ui.service.UIReportServiceCallback;
import com.payway.webapp.reporting.ui.service.content.ReportContentService;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * AdvertisingReportingWorkspace
 *
 * @author Sergey Kichenko
 * @created 07.08.2015
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = AdvertisingReportingWorkspace.ADVERTISING_REPORTING_WORKSPACE_VIEW_ID)
public class AdvertisingReportingWorkspace extends AbstractAdvertisingWorkspaceView {

    public static final String ADVERTISING_REPORTING_WORKSPACE_VIEW_ID = ADVERTISING_DEFAULT_WORKSPACE_VIEW_PREFIX + "Reporting";

    private static final long serialVersionUID = 8840023165300248995L;

    @Autowired
    private UIReportService reporting;

    @Autowired
    private ReportContentService service;

    @UiField
    private TextField editReportNo;

    @PostConstruct
    public void postConstruct() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("AdvertisingReportingWorkspace.xml", this));
        editReportNo.setValue("702079960");
    }

    @Override
    public void activate() {
        //
    }

    @UiHandler(value = "btnReport")
    public void onClickReport(Button.ClickEvent event) {

        //702079960
        reporting.execute(Long.parseLong(editReportNo.getValue()), new UIReportServiceCallback() {

            @Override
            public void begin() {
                ((InteractionUI) UI.getCurrent()).showProgressBar();
            }

            @Override
            public void end() {
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }

            @Override
            public void response(String fileName, byte[] content) {
                try {

                    String servletPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
                    ReportContentService.ReportContentInfo info = service.save(UI.getCurrent().getSession().getSession().getId(), fileName, content);

                    Link link = new Link(fileName, new ExternalResource(servletPath + "/reporting/" + info.getRelativeURLPath()));
                    link.setTargetName("_blank");
                    HorizontalLayout layout = new HorizontalLayout(link);

                    layout.setSpacing(true);

                    new WindowBuilder()
                            .withCaption("Report content")
                            .withContent(layout)
                            .withModal()
                            .withClosable()
                            .withSizeUndefined()
                            .buildAndShow();
                } catch (Exception ex) {
                    log.error("Cannot save report content on server response", ex);
                }
            }

            @Override
            public void exception(Exception ex) {
                ((InteractionUI) UI.getCurrent()).showNotification("Reporting", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }
        }
        );
    }
}
