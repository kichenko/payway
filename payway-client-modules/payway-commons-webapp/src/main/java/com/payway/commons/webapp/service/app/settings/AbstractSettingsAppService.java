/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.service.app.settings;

import com.payway.commons.webapp.service.app.settings.model.AbstractAppSessionSettings;
import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.bus.AppEventPublisher;
import com.payway.commons.webapp.config.SubscribeOnAppEventBus;
import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.web.event.ApplicationStartClientConnectedEvent;
import com.payway.messaging.message.SettingsChangedMessage;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;
import com.vaadin.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * AbstractSettingsAppService
 *
 * @author Sergey Kichenko
 * @param <T>
 * @created 06.07.15 00:00
 */
@Slf4j
@SubscribeOnAppEventBus
public abstract class AbstractSettingsAppService<T extends AbstractAppSessionSettings> implements SettingsAppService<T>, ApplicationListener<ApplicationStartClientConnectedEvent> {

    @Autowired
    protected MessageServerSenderService sender;

    @Autowired
    protected AppEventPublisher appEventPublisher;

    protected CurrencyDto currency;
    protected MoneyPrecisionDto moneyPrecision;

    protected abstract void doLoadRemoteConfiguration();

    protected T doGetSessionSettings() {

        T settings = null;
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            settings = (T) session.getAttribute(CommonAttributes.SESSION_SETTINGS.value());
        }

        return settings;
    }

    protected boolean doSetSessionSettings(T settings) {

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(CommonAttributes.SESSION_SETTINGS.value(), settings);
            return true;
        }

        return false;
    }

    @Override
    public T getSessionSettings() {
        return doGetSessionSettings();
    }

    @Override
    public boolean setSessionSettings(T settings) {
        return doSetSessionSettings(settings);
    }

    @Subscribe
    public void onMessage(SettingsChangedMessage message) {

        if (log.isDebugEnabled()) {
            log.debug("Receive settings change mesage - {}", message);
        }

        doLoadRemoteConfiguration();
    }

    @Override
    public void onApplicationEvent(ApplicationStartClientConnectedEvent event) {
        doLoadRemoteConfiguration();
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
