/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.kioskcashier.SettingsDto;
import lombok.Getter;
import lombok.ToString;

/**
 * KioskCashierSettingsResponse
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class KioskCashierSettingsResponse implements SuccessResponse {

    private static final long serialVersionUID = -8060686512584612365L;

    private final SettingsDto settings;

    public KioskCashierSettingsResponse(SettingsDto settings) {
        this.settings = settings;
    }

}
