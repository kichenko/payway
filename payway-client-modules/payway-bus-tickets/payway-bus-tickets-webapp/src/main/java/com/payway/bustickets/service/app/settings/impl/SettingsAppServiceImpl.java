/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.settings.impl;

import com.payway.bustickets.service.app.settings.model.BusTicketsSessionSettings;
import com.payway.commons.webapp.bus.event.SettingsChangedAppEventBus;
import com.payway.commons.webapp.messaging.ResponseCallbackSupport;
import com.payway.commons.webapp.service.app.settings.AbstractSettingsAppService;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.message.bustickets.BusTicketSettingsRequest;
import com.payway.messaging.message.bustickets.BusTicketSettingsResponse;
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
public class SettingsAppServiceImpl extends AbstractSettingsAppService<BusTicketsSessionSettings> {

    @Override
    protected void doLoadRemoteConfiguration() {

        if (log.isDebugEnabled()) {
            log.debug("Send settings request");
        }

        sender.sendMessage(new BusTicketSettingsRequest(), new ResponseCallbackSupport<BusTicketSettingsResponse, ExceptionResponse>() {
            @Override
            public void onServerResponse(BusTicketSettingsResponse response) {

                if (log.isDebugEnabled()) {
                    log.debug("Receive remote settings - {}", response);
                }

                setCurrency(response.getSettings().getCurrency());
                setMoneyPrecision(response.getSettings().getMoneyPrecision());

                //notify about settigs changed, need for ui's
                appEventPublisher.sendNotification(new SettingsChangedAppEventBus());
            }
        });
    }
}
