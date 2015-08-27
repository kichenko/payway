/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.reporting;

import com.payway.advertising.ui.component.notification.events.report.ExecuteReportNotificationEvent;
import com.payway.advertising.ui.component.notification.events.report.ExecuteReportStatusType;
import com.payway.advertising.ui.view.core.AbstractAdvertisingWorkspaceView;
import com.payway.advertising.ui.view.workspace.reporting.container.ReportPagingBeanContainer;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.components.table.paging.IPagingContainer;
import com.payway.commons.webapp.ui.components.table.paging.PagingTableControls;
import com.payway.commons.webapp.ui.components.table.paging.PagingTableImpl;
import com.payway.messaging.model.reporting.ReportDto;
import com.payway.messaging.model.reporting.ReportExportFormatTypeDto;
import com.payway.messaging.model.reporting.ReportParameterDto;
import com.payway.webapp.reporting.ui.service.UIReportService;
import com.payway.webapp.reporting.ui.service.UIReportServiceMetaDataCallback;
import com.payway.webapp.reporting.ui.service.UIReportServiceReportCallback;
import com.payway.webapp.reporting.ui.service.content.ReportContentService;
import com.payway.webapp.settings.WebAppSettingsService;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UIReportService reportingService;

    @Autowired
    private ReportContentService reportingContentService;

    @Autowired
    private MessageServerSenderService messageService;

    @Autowired
    private WebAppUserService webAppUserService;

    @Autowired
    protected SessionEventBus sessionEventBus;

    @Autowired
    protected WebAppSettingsService settingsService;

    @Value("${app.config.reporting.rest.path:/reporting}")
    private String restPath;

    @Getter
    @Setter
    @Value("SECONDS")
    private TimeUnit unit;

    /**
     * Time to wait response message from server, if timeout - log & free all
     * locks.
     */
    @Getter
    @Setter
    @Value("300")
    private long serverTimeOut;

    @UiField
    private PagingTableImpl gridReports;

    @UiField
    private PagingTableControls pagingControls;

    @UiField
    private TextField txtSearch;

    private ReportPagingBeanContainer gridContainer;

    @PostConstruct
    public void postConstruct() {

        init();

        txtSearch.setIcon(FontAwesome.SEARCH);

        gridReports.setImmediate(true);
        gridReports.setSelectable(true);

        gridContainer = new ReportPagingBeanContainer(ReportDto.class, messageService, webAppUserService, serverTimeOut, unit);
        gridContainer.setErrorListener(new IPagingContainer.IPagingLoadCallback() {
            @Override
            public void exception(Exception ex) {
                log.error("Load reports", ex);
                ((InteractionUI) UI.getCurrent()).showNotification("Load reports", "Error loading reports", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void begin() {
                ((InteractionUI) UI.getCurrent()).showProgressBar();
            }

            @Override
            public void end() {
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }
        });

        gridReports.setContainerDataSource(gridContainer);
        pagingControls.setPagingTable(gridReports);

        gridReports.addGeneratedColumn("name", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return ((BeanItem<ReportDto>) gridContainer.getItem(itemId)).getBean().getName();
            }
        });

        gridReports.addGeneratedColumn("description", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return ((BeanItem<ReportDto>) gridContainer.getItem(itemId)).getBean().getDescription();
            }
        });

        gridReports.addGeneratedColumn("run", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, final Object itemId, Object columnId) {

                Button btn = new Button(FontAwesome.PLAY);
                btn.addStyleName("tiny");
                btn.addStyleName("friendly");
                btn.addStyleName("icon-only");

                btn.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 5019806363620874205L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        actionRunReport(itemId);
                    }
                });

                HorizontalLayout layout = new HorizontalLayout(btn);
                layout.setSizeFull();
                layout.setComponentAlignment(btn, Alignment.MIDDLE_CENTER);

                return layout;
            }
        });

        //set column view
        gridReports.setColumnHeader("name", "Name");
        gridReports.setColumnHeader("description", "Description");
        gridReports.setColumnHeader("run", "Action");

        gridReports.setColumnAlignment("run", Table.Align.CENTER);
        
        gridReports.setColumnExpandRatio("name", 0.25F);
        gridReports.setColumnExpandRatio("description", 0.7F);
        gridReports.setColumnExpandRatio("run", 0.05F);

        gridReports.setVisibleColumns("name", "description", "run");
        gridReports.sort(new Object[]{"name"}, new boolean[]{true});
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("AdvertisingReportingWorkspace.xml", this));
    }

    @Override
    public void activate() {
        getMenuBar().setVisible(false);
        getButtonFileUploadToolBar().setVisible(false);
        gridReports.refresh();
    }

    private void actionRunReport(Object itemId) {

        reportingService.execute(
                gridContainer.getItem(itemId).getBean().getId(),
                new UIReportServiceMetaDataCallback() {

                    @Override
                    public void begin() {
                        ((InteractionUI) UI.getCurrent()).showProgressBar();
                    }

                    @Override
                    public void end() {
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                    }

                    @Override
                    public void exception(Exception ex) {
                        ((InteractionUI) UI.getCurrent()).showNotification("Reporting", "Internal server error", Notification.Type.ERROR_MESSAGE);
                    }
                },
                new UIReportServiceReportCallback() {

                    private UUID uid;
                    private LocalDateTime start;
                    private String reportName;

                    @Override
                    public void metadata(long id, String name, boolean ignorePagination, ReportExportFormatTypeDto format, List<ReportParameterDto> params) {
                        reportName = name;
                    }

                    @Override
                    public void begin() {
                        uid = UUID.randomUUID();
                        start = new LocalDateTime();
                        sessionEventBus.sendNotification(new ExecuteReportNotificationEvent(uid, reportName, webAppUserService.getUser().getLogin(), start, new LocalDateTime(), ExecuteReportStatusType.Run));
                    }

                    @Override
                    public void end() {
                        //
                    }

                    @Override
                    public void response(String fileName, byte[] content) {

                        try {
                            ReportContentService.ReportContentInfo info = reportingContentService.save(UI.getCurrent().getSession().getSession().getId(), fileName, content);
                            String url = VaadinServlet.getCurrent().getServletContext().getContextPath() + StringUtils.prependIfMissingIgnoreCase(StringUtils.appendIfMissingIgnoreCase(restPath, "/"), "/") + info.getRelativeURLPath();
                            sessionEventBus.sendNotification(new ExecuteReportNotificationEvent(uid, reportName, webAppUserService.getUser().getLogin(), start, new LocalDateTime(), ExecuteReportStatusType.Success, new Object[]{url}));
                        } catch (Exception ex) {
                            log.error("Cannot save report content on server response", ex);
                            sessionEventBus.sendNotification(new ExecuteReportNotificationEvent(uid, reportName, webAppUserService.getUser().getLogin(), start, new LocalDateTime(), ExecuteReportStatusType.Fail, new Object[]{ex}));
                        }
                    }

                    @Override
                    public void exception(Exception ex) {
                        sessionEventBus.sendNotification(new ExecuteReportNotificationEvent(uid, reportName, webAppUserService.getUser().getLogin(), start, new LocalDateTime(), ExecuteReportStatusType.Fail, new Object[]{ex}));
                    }

                }
        );
    }

    @UiHandler("txtSearch")
    public void onTextFieldSearchChange(FieldEvents.TextChangeEvent event) {

        if (event.getText() != null && !event.getText().isEmpty()) {
            gridReports.addCriteria(ReportPagingBeanContainer.CRITERIA_NAME_AND_DESCRIPTION, event.getText());
        } else {
            gridReports.removeCriteria(ReportPagingBeanContainer.CRITERIA_NAME_AND_DESCRIPTION);
        }
    }
}
