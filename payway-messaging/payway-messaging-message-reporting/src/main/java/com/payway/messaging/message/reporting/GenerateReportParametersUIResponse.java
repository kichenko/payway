/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.reporting.ReportUIDto;
import lombok.ToString;

/**
 * GenerateReportParametersUIResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@ToString(callSuper = true)
public final class GenerateReportParametersUIResponse implements SuccessResponse {

    private static final long serialVersionUID = -6380555138849965967L;

    private final ReportUIDto reportUi;

    public GenerateReportParametersUIResponse(ReportUIDto reportUi) {
        this.reportUi = reportUi;
    }

    public ReportUIDto getReportUi() {
        return reportUi;
    }

}
