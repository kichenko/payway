/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.service.app.settings;

import com.payway.commons.webapp.service.app.settings.AbstractSettingsAppService;
import com.payway.kioskcashier.service.app.settings.model.KioskCashierSessionSettings;
import com.payway.messaging.model.common.BankAccountDto;
import lombok.Getter;
import lombok.Setter;

/**
 * AbstractKioskCashierSettingsAppService
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Getter
@Setter
public abstract class AbstractKioskCashierSettingsAppService extends AbstractSettingsAppService<KioskCashierSessionSettings> {

    protected BankAccountDto accountCashDeposit;
}
