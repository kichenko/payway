/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentReportFailureSearchRequest
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class EncashmentsResultFailureResponse implements SuccessResponse {

    private static final long serialVersionUID = 2146273946116019802L;

    private final String reason;
}
