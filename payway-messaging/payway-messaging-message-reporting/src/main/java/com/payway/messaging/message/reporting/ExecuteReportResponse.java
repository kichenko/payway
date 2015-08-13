/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.reporting;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * ExecuteReportResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class ExecuteReportResponse implements SuccessResponse {

    private static final long serialVersionUID = -6139587779688956792L;
    
    private final String fileName;
    private final byte[] content;
}
