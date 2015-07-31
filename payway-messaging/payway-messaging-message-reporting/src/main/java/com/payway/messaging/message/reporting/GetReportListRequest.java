/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * GetReportListRequest
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@NoArgsConstructor
@ToString(callSuper = true)
public final class GetReportListRequest extends CommandRequest {

    private static final long serialVersionUID = -2990884617758127707L;

}
