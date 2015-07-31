/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * QuickEncashmentCheckResponse
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public class QuickEncashmentCheckResponse implements SuccessResponse {

    private static final long serialVersionUID = 798083976749329168L;

    private final long encashmentId;
    private final double amount;

}
