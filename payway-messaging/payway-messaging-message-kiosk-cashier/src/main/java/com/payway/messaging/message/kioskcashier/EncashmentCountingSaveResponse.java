/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentCountingSaveResponse
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class EncashmentCountingSaveResponse implements SuccessResponse {

    private static final long serialVersionUID = 4838381928224679017L;

    private final long countingId;
    private final double shortage;
    private final double surplus;

    public EncashmentCountingSaveResponse(long countingId, double surplus, double shortage) {
        this.countingId = countingId;
        this.surplus = surplus;
        this.shortage = shortage;
    }
}
