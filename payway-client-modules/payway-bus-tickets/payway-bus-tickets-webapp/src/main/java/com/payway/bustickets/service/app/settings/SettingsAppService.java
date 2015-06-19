/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.settings;

import com.payway.bustickets.core.BusTicketsSettings;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;

/**
 * SettingsAppService
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
public interface SettingsAppService {

    BusTicketsSettings getBusTicketsSettings();

    boolean setBusTicketsSettings(BusTicketsSettings settings);

    void setCurrency(CurrencyDto dto);

    CurrencyDto getCurrency();

    void setMoneyPrecision(MoneyPrecisionDto dto);

    MoneyPrecisionDto getMoneyPrecision();
}
