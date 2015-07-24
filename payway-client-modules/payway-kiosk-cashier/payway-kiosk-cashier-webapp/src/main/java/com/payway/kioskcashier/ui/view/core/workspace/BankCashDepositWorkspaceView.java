/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.view.core.workspace;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.bus.event.SettingsChangedAppEventBus;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.BankCashDepositWizard;
import com.payway.kioskcashier.ui.view.core.AbstractKioskCashierWorkspaceView;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BankCashDepositWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = BankCashDepositWorkspaceView.BANK_CASH_DEPOSIT_WORKSPACE_VIEW_ID)
public class BankCashDepositWorkspaceView extends AbstractKioskCashierWorkspaceView {

    public static final String BANK_CASH_DEPOSIT_WORKSPACE_VIEW_ID = WORKSPACE_VIEW_ID_PREFIX + "bank-cash-deposit";

    private static final long serialVersionUID = 1883807156208070210L;

    @UiField
    private BankCashDepositWizard wizard;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("BankCashDepositWorkspaceView.xml", this));
    }

    @Override
    public void activate() {
        wizard.setService(service);
        wizard.setCurrency(settingsAppService.getCurrency());
        wizard.setSessionId(userAppService.getUser().getSessionId());
        wizard.setAccountCashDeposit(settingsAppService.getAccountCashDeposit());

        wizard.activate();
    }

    private void setWizardChangedSettings() {
        wizard.setCurrency(settingsAppService.getCurrency());
        wizard.setAccountCashDeposit(settingsAppService.getAccountCashDeposit());
    }

    @Subscribe
    public void onSettingsChanged(SettingsChangedAppEventBus event) {

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {

                if (log.isDebugEnabled()) {
                    log.debug("Apply changed settings for workspace view");
                }

                setWizardChangedSettings();
            }
        });
    }
}
