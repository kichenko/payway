/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.model.reporting.ReportParametersDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * ExecuteReportRequest
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class ExecuteReportRequest extends CommandRequest {

    private static final long serialVersionUID = -8434907900432366594L;

    private final ReportParametersDto reportParams;
}
