/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.reporting.ReportUIDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * GenerateReportParametersUIResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class GenerateReportParametersUIResponse implements SuccessResponse {

    private static final long serialVersionUID = -6380555138849965967L;

    private final ReportUIDto reportUi;
}
