/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.workspace;

import com.payway.bustickets.airportexpress.ui.components.BusTicketsWizard;
import com.payway.bustickets.service.app.AppService;
import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.messaging.model.common.OperatorDto;
import com.vaadin.spring.annotation.UIScope;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AirportExpressWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@UIScope
@Component(value = AirportExpressWorkspaceView.BUS_TICKET_AIRPORT_EXPRESS_WORKSPACE_VIEW_ID)
public class AirportExpressWorkspaceView extends AbstractBusTicketsWorkspaceView {

    public static final String BUS_TICKET_AIRPORT_EXPRESS_WORKSPACE_VIEW_ID = WORKSPACE_VIEW_ID_PREFIX + "stalwart";

    private static final long serialVersionUID = 4644909330724328135L;

    @UiField
    private BusTicketsWizard wizard;

    @Autowired
    @Qualifier("messageServerSenderService")
    private MessageServerSenderService service;

    @Autowired
    @Qualifier("appService")
    private AppService appService;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("AirportExpressWorkspaceView.xml", this));
        wizard.setService(service);
    }

    @Override
    public void activate() {
        
        setWizardTerminals();
        setWizardOperatorId();
        setWizardSessionId();
        setWizardLogoImage();

        if (wizard.setStep(BusTicketsWizard.BUS_TICKETS_PARAMS_WIZARD_STEP_ID)) {
            wizard.setUpBusTicketsPaymentParams();
        }
    }

    private void setWizardOperatorId() {

        SideBarMenu.MenuItem menu = this.getSideBarMenu().getSelectedMenuItem();
        if (menu == null) {
            return;
        }

        wizard.setOperatorId((String) menu.getData());
    }

    private void setWizardTerminals() {
        wizard.setUpTerminals(appService.getBusTicketsSettings().getTerminals());
    }

    private void setWizardSessionId() {

        String sessionId = appService.getBusTicketsSettings().getSessionId();
        if (StringUtils.isBlank(sessionId)) {
            log.warn("Empty sessionId in session storage");
            return;
        }

        wizard.setSessionId(sessionId);
    }

    private void setWizardLogoImage() {

        SideBarMenu.MenuItem menuItem = this.getSideBarMenu().getSelectedMenuItem();
        if (menuItem == null) {
            return;
        }

        for (OperatorDto operator : appService.getBusTicketsSettings().getOperators()) {
            if (!StringUtils.isBlank(operator.getShortName())) {
                if (operator.getShortName().equals((String) menuItem.getData())) {
                    if (operator.getLogo() != null && operator.getLogo().getContent() != null) {
                        wizard.setLogoImage(operator.getLogo().getContent());
                    }
                    break;
                }
            }
        }
    }
}
