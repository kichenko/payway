/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.view.workspace;

import com.google.common.eventbus.Subscribe;
import com.payway.bustickets.service.app.settings.model.BusTicketsSessionSettings;
import com.payway.bustickets.ui.components.BusTicketsWizard;
import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import com.payway.commons.webapp.bus.event.SettingsChangedAppEventBus;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.service.app.settings.SettingsAppService;
import com.payway.commons.webapp.service.app.user.WebAppUserService;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.messaging.model.common.OperatorDto;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BusTicketsWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = BusTicketsWorkspaceView.BUS_TICKETS_WORKSPACE_VIEW_ID)
public class BusTicketsWorkspaceView extends AbstractBusTicketsWorkspaceView {

    public static final String BUS_TICKETS_WORKSPACE_VIEW_ID = WORKSPACE_VIEW_ID_PREFIX + "Tickets";

    private static final long serialVersionUID = 4644909330724328135L;

    @UiField
    private BusTicketsWizard wizard;

    @Autowired
    private MessageServerSenderService service;

    @Autowired
    private SettingsAppService<BusTicketsSessionSettings> settingsAppService;

    @Autowired
    protected WebAppUserService userAppService;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsWorkspaceView.xml", this));
        wizard.setService(service);
    }

    @Override
    public void activate() {

        setWizardCurrentTerminal();
        setWizardOperatorId();
        setWizardSessionId();
        setWizardLogoImage();
        setWizardCurrencyAndMoneyPrecision();

        if (wizard.setStep(BusTicketsWizard.WizardStepType.Params.ordinal())) {
            wizard.setUpBusTicketsPaymentParams();
        }
    }

    private void setWizardOperatorId() {

        SideBarMenu.MenuItem menu = this.getSideBarMenu().getSelectedMenuItem();
        if (menu == null) {
            return;
        }

        wizard.setOperatorId((Long) menu.getData());
    }

    private void setWizardCurrentTerminal() {
        wizard.setUpCurrentTerminal(settingsAppService.getSessionSettings().getCurrentRetailerTerminal());
    }

    private void setWizardSessionId() {
        wizard.setSessionId(userAppService.getUser().getSessionId());
    }

    private void setWizardLogoImage() {

        SideBarMenu.MenuItem menuItem = this.getSideBarMenu().getSelectedMenuItem();
        if (menuItem == null) {
            return;
        }

        for (OperatorDto operator : settingsAppService.getSessionSettings().getOperators()) {
            if (operator.getId() == (long) menuItem.getData()) {
                if (operator.getLogo() != null && operator.getLogo().getContent() != null) {
                    wizard.setLogoImage(operator.getLogo().getContent());
                }
                break;
            }
        }
    }

    private void setWizardCurrencyAndMoneyPrecision() {
        wizard.setUpCurrencyAndMoneyPresicion(settingsAppService.getCurrency(), settingsAppService.getMoneyPrecision());
    }

    @Subscribe
    public void onSettingsChanged(SettingsChangedAppEventBus event) {

        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {

                if (log.isDebugEnabled()) {
                    log.debug("Apply changed settings for workspace view");
                }

                setWizardCurrencyAndMoneyPrecision();
            }
        });
    }
}
