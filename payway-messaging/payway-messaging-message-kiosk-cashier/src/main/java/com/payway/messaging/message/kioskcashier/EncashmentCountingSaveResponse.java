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

    private final boolean surplus;
    private final boolean shortage;

    public EncashmentCountingSaveResponse(boolean surplus, boolean shortage) {
        this.surplus = surplus;
        this.shortage = shortage;
    }
}
