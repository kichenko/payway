/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.reporting.container;

import com.google.common.collect.Lists;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.commons.webapp.ui.components.table.paging.AbstractPagingBeanContainer;
import com.payway.messaging.core.response.Response;
import com.payway.messaging.message.reporting.GetReportListQueryRequest;
import com.payway.messaging.message.reporting.GetReportListQueryResponse;
import com.payway.messaging.model.common.data.OrderDto;
import com.payway.messaging.model.reporting.ReportDto;
import com.vaadin.data.util.BeanItem;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ReportPagingBeanContainer
 *
 * @author Sergey Kichenko
 * @created 12.08.2015
 */
@Slf4j
public class ReportPagingBeanContainer extends AbstractPagingBeanContainer<Long, ReportDto> {

    private static final long serialVersionUID = -3718950667194246889L;

    public static final String CRITERIA_NAME_AND_DESCRIPTION = "name&description";

    @Setter
    @Getter
    private MessageServerSenderService service;

    @Setter
    @Getter
    private WebAppUserService webAppUserService;

    @Getter
    @Setter
    private TimeUnit unit;

    /**
     * Time to wait response message from server, if timeout - log & free all
     * locks.
     */
    @Getter
    @Setter
    private long serverTimeOut;

    private ReportPagingBeanContainer() {
        super(ReportDto.class);
    }

    public ReportPagingBeanContainer(Class<ReportDto> type, MessageServerSenderService service, WebAppUserService webAppUserService, long serverTimeOut, TimeUnit unit) {
        super(type);

        setUnit(unit);
        setService(service);
        setWebAppUserService(webAppUserService);
        setServerTimeOut(serverTimeOut);
    }

    @Override
    protected boolean load() {

        boolean success = false;

        if (getErrorListener() != null) {
            getErrorListener().begin();
        }

        internalRemoveAllItems();

        try {

            //TODO: Warning - timeout used if server never answered
            Response response = service.sendMessage(
                    new GetReportListQueryRequest(
                            webAppUserService.getUser().getSessionId(),
                            sort == null ? new ArrayList<OrderDto>(0) : Lists.newArrayList(Lists.transform(Lists.newArrayList(sort.iterator()), new TransforemerSpringOrder2OrderDto())),
                            (String) criteries.get(CRITERIA_NAME_AND_DESCRIPTION),
                            currentPage * pageSize, pageSize
                    ),
                    getServerTimeOut(), getUnit()
            );

            if (response instanceof GetReportListQueryResponse) {

                GetReportListQueryResponse rsp = (GetReportListQueryResponse) response;

                total = rsp.getCount();
                if (currentPage >= getTotalPages()) {
                    currentPage = (getTotalPages() - 1) < 0 ? 0 : getTotalPages() - 1;
                }

                for (ReportDto bean : rsp.getReports()) {
                    internalAddItemAtEnd(bean.getId(), new BeanItem<>(bean), false);
                }
                success = true;
            } else {
                if (getErrorListener() != null) {
                    getErrorListener().exception(new Exception());
                }
            }

            if (getErrorListener() != null) {
                getErrorListener().end();
            }

        } catch (Exception ex) {
            log.error("Cannot receive reports from server - {}", ex.getMessage());
            if (getErrorListener() != null) {
                getErrorListener().exception(new Exception(ex.getMessage()));
                getErrorListener().end();
            }
        }

        return success;
    }
}
