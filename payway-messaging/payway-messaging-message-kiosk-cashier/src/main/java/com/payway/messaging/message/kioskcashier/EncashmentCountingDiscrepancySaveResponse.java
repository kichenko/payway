/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * EncashmentCountingDiscrepancySaveResponse
 *
 * @author Sergey Kichenko
 * @created 14.07.15 00:00
 */
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public final class EncashmentCountingDiscrepancySaveResponse implements SuccessResponse {

    private static final long serialVersionUID = -6861903538090965609L;
}
