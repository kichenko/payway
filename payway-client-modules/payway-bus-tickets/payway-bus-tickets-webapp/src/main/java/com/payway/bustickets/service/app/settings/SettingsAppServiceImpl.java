/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.settings;

import com.google.common.eventbus.Subscribe;
import com.payway.bustickets.core.BusTicketAttributes;
import com.payway.bustickets.core.BusTicketsSessionSettings;
import com.payway.commons.webapp.bus.AppEventPublisher;
import com.payway.commons.webapp.bus.event.SettingsChangedAppEventBus;
import com.payway.commons.webapp.config.SubscribeOnAppEventBus;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.ResponseCallbackSupport;
import com.payway.commons.webapp.web.event.ApplicationStartClientConnectedEvent;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.message.SettingsChangedMessage;
import com.payway.messaging.message.bustickets.BusTicketSettingsRequest;
import com.payway.messaging.message.bustickets.BusTicketSettingsResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;
import com.vaadin.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * SettingsAppServiceImpl
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
@Slf4j
@SubscribeOnAppEventBus
@Component(value = "settingsAppService")
public class SettingsAppServiceImpl implements SettingsAppService, ApplicationListener<ApplicationStartClientConnectedEvent> {

    @Autowired
    private MessageServerSenderService sender;

    private CurrencyDto currency;
    private MoneyPrecisionDto moneyPrecision;

    @Autowired
    private AppEventPublisher appEventPublisher;

    @Override
    public BusTicketsSessionSettings getSessionSettings() {

        BusTicketsSessionSettings settings = null;
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            settings = (BusTicketsSessionSettings) session.getAttribute(BusTicketAttributes.BUS_TICKET_SESSION_SETTINGS.value());
        }

        return settings;
    }

    @Override
    public boolean setSessionSettings(BusTicketsSessionSettings settings) {

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(BusTicketAttributes.BUS_TICKET_SESSION_SETTINGS.value(), settings);
            return true;
        }

        return false;
    }

    @Subscribe
    public void onMessage(SettingsChangedMessage message) {

        if (log.isDebugEnabled()) {
            log.debug("Receive settings change mesage - {}", message);
        }

        loadRemoteConfiguration();
    }

    @Override
    public void onApplicationEvent(ApplicationStartClientConnectedEvent event) {
        loadRemoteConfiguration();
    }

    private void loadRemoteConfiguration() {

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

    @Override
    public void setCurrency(CurrencyDto dto) {
        currency = dto;
    }

    @Override
    public CurrencyDto getCurrency() {
        return currency;
    }

    @Override
    public void setMoneyPrecision(MoneyPrecisionDto dto) {
        moneyPrecision = dto;
    }

    @Override
    public MoneyPrecisionDto getMoneyPrecision() {
        return moneyPrecision;
    }
}
