/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.reporting.ReportDto;
import lombok.ToString;

import java.util.List;

/**
 * GetReportListResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@ToString(callSuper = true)
public final class GetReportListQueryResponse implements SuccessResponse {

    private static final long serialVersionUID = -2045473704810143010L;

    private final long count;

    private final List<ReportDto> reports;

    public GetReportListQueryResponse(long count, List<ReportDto> reports) {
        this.count = count;
        this.reports = reports;
    }

    public long getCount() {
        return count;
    }

    public List<ReportDto> getReports() {
        return reports;
    }

}
