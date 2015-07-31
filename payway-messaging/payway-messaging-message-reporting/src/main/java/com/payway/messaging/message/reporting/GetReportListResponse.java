/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.Getter;
import lombok.ToString;

/**
 * GetReportListResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class GetReportListResponse implements SuccessResponse {

    private static final long serialVersionUID = -2045473704810143010L;

}
