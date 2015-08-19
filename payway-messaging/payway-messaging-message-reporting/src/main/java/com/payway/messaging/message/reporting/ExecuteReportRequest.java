/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.message.SessionCommandRequest;
import com.payway.messaging.model.reporting.ReportExecuteParamsDto;
import lombok.Getter;
import lombok.ToString;

/**
 * ExecuteReportRequest
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@ToString(callSuper = true)
public final class ExecuteReportRequest extends SessionCommandRequest {

    private static final long serialVersionUID = -8434907900432366594L;

    private final ReportExecuteParamsDto data;

    public ExecuteReportRequest(String sessionId, ReportExecuteParamsDto data) {
        super(sessionId);
        this.data = data;
    }

    public ReportExecuteParamsDto getData() {
        return data;
    }

}
