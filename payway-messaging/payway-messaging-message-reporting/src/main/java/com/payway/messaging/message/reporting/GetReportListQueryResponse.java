/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.reporting.ReportDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * GetReportListResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class GetReportListQueryResponse implements SuccessResponse {

    private static final long serialVersionUID = -2045473704810143010L;

    private final long count;
    private final List<ReportDto> reports;
}
