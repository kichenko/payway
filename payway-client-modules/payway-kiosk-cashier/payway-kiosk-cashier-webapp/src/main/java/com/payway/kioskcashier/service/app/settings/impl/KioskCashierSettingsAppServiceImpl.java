/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.service.app.settings.impl;

import com.payway.commons.webapp.bus.event.SettingsChangedAppEventBus;
import com.payway.commons.webapp.messaging.ResponseCallbackSupport;
import com.payway.kioskcashier.service.app.settings.AbstractKioskCashierSettingsAppService;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.message.kioskcashier.KioskCashierSettingsRequest;
import com.payway.messaging.message.kioskcashier.KioskCashierSettingsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SettingsAppServiceImpl
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Slf4j
@Component(value = "settingsAppService")
public class KioskCashierSettingsAppServiceImpl extends AbstractKioskCashierSettingsAppService {

    @Override
    protected void doLoadRemoteConfiguration() {

        if (log.isDebugEnabled()) {
            log.debug("Send settings request");
        }

        sender.sendMessage(new KioskCashierSettingsRequest(), new ResponseCallbackSupport<KioskCashierSettingsResponse, ExceptionResponse>() {
            @Override
            public void onServerResponse(KioskCashierSettingsResponse response) {

                if (log.isDebugEnabled()) {
                    log.debug("Receive remote settings - {}", response);
                }

                setCurrency(response.getSettings().getCurrency());
                setAccountCashDeposit(response.getSettings().getAccountCashDeposit());
                setMoneyPrecision(response.getSettings().getMoneyPrecision());

                //notify about settigs changed, need for ui's
                appEventPublisher.sendNotification(new SettingsChangedAppEventBus());
            }
        });
    }
}
