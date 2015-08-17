/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.Getter;
import lombok.ToString;

/**
 * GenerateReportParametersUIRequest
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@ToString(callSuper = true)
public final class GenerateReportParametersUIRequest extends CommandRequest {

    private static final long serialVersionUID = 1194176494709161854L;

    private final long reportId;

    public GenerateReportParametersUIRequest(long reportId) {
        this.reportId = reportId;
    }

    public long getReportId() {
        return reportId;
    }

}
