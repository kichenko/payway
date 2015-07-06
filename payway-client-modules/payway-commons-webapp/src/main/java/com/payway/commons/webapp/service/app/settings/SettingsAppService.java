/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.service.app.settings;

import com.payway.commons.webapp.service.app.settings.model.AbstractAppSessionSettings;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;

/**
 * SettingsAppService
 *
 * @author Sergey Kichenko
 * @param <T>
 * @created 06.07.15 00:00
 */
public interface SettingsAppService<T extends AbstractAppSessionSettings> {

    T getSessionSettings();

    boolean setSessionSettings(T settings);

    void setCurrency(CurrencyDto dto);

    CurrencyDto getCurrency();

    void setMoneyPrecision(MoneyPrecisionDto dto);

    MoneyPrecisionDto getMoneyPrecision();
}
