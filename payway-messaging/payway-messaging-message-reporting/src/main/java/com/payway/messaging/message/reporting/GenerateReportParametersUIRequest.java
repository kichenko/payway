/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * GenerateReportParametersUIRequest
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class GenerateReportParametersUIRequest extends CommandRequest {

    private static final long serialVersionUID = 1194176494709161854L;

    private final long reportId;

}
