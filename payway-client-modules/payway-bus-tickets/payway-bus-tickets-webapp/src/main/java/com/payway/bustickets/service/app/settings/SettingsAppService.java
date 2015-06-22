/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.settings;

import com.payway.bustickets.core.BusTicketsSessionSettings;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;

/**
 * SettingsAppService
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
public interface SettingsAppService {

    BusTicketsSessionSettings getSessionSettings();

    boolean setSessionSettings(BusTicketsSessionSettings settings);

    void setCurrency(CurrencyDto dto);

    CurrencyDto getCurrency();

    void setMoneyPrecision(MoneyPrecisionDto dto);

    MoneyPrecisionDto getMoneyPrecision();
}
