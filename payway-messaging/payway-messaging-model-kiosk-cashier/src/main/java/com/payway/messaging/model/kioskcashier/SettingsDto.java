/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.AbstractDto;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;
import lombok.Getter;
import lombok.ToString;

/**
 * SettingsDto
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class SettingsDto extends AbstractDto {

    private static final long serialVersionUID = 2020821325401263476L;

    private final CurrencyDto currency;

    private final MoneyPrecisionDto moneyPrecision;

    public SettingsDto(CurrencyDto currency, MoneyPrecisionDto moneyPrecision) {
        this.currency = currency;
        this.moneyPrecision = moneyPrecision;
    }

}
