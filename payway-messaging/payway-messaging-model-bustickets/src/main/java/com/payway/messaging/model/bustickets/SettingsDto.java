/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.bustickets;

import com.payway.messaging.model.AbstractDto;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * SettingsDto
 *
 * @author Sergey Kichenko
 * @created 19.06.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class SettingsDto extends AbstractDto {

    private static final long serialVersionUID = -8936272452512462276L;

    private final CurrencyDto currency;

    private final MoneyPrecisionDto moneyPrecision;

    private final int baggageRatio;

    public SettingsDto(CurrencyDto currency, MoneyPrecisionDto moneyPrecision, int baggageRatio) {
        this.currency = currency;
        this.moneyPrecision = moneyPrecision;
        this.baggageRatio = baggageRatio;
    }
}
