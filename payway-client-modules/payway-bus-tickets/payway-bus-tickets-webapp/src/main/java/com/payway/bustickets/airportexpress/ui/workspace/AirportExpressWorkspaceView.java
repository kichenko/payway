/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.workspace;

import com.payway.bustickets.airportexpress.ui.components.BusTicketsParamsWizardStep;
import com.payway.bustickets.airportexpress.ui.components.BusTicketsWizard;
import com.payway.commons.webapp.messaging.UIResponseCallBackImpl;
import com.payway.bustickets.service.app.AppService;
import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.bustickets.BusTicketPaymentStartRequest;
import com.payway.messaging.message.bustickets.BusTicketPaymentStartResponse;
import com.payway.messaging.model.common.OperatorDto;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
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

        setWizardLogoImage();
        if (wizard.setStep(BusTicketsParamsWizardStep.STEP_NO)) {
            sendBusTicketPaymentStartRequest();
        }
    }

    private void setWizardLogoImage() {

        SideBarMenu.MenuItem menuItem = this.getSideBarMenu().getSelectedMenuItem();
        if (menuItem == null) {
            return;
        }

        for (OperatorDto operator : appService.getUserBusTicketOperators()) {
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

    private void sendBusTicketPaymentStartRequest() {

        SideBarMenu.MenuItem menuItem = this.getSideBarMenu().getSelectedMenuItem();
        if (menuItem != null && menuItem.getData() instanceof String) {
            ((InteractionUI) UI.getCurrent()).showProgressBar();
            service.sendMessage(new BusTicketPaymentStartRequest((String) menuItem.getData()), new UIResponseCallBackImpl(getUI(), new UIResponseCallBackImpl.ResponseCallbackHandler() {

                @Override
                public void doServerResponse(SuccessResponse response) {
                    if (response instanceof BusTicketPaymentStartResponse) {
                        processSuccessBusTicketPaymentStartRequest((BusTicketPaymentStartResponse) response);
                    } else {
                        log.error("Bad server response (unknown type) - {}", response);
                    }
                }

                @Override
                public void doServerException(ExceptionResponse exception) {
                    log.error("Bad server response (server exception) - {}", exception);
                    processFailBusTicketPaymentStartRequest();
                }

                @Override
                public void doLocalException(Exception exception) {
                    log.error("Bad server response (local exception) - {}", exception);
                    processFailBusTicketPaymentStartRequest();
                }

                @Override
                public void doTimeout() {
                    log.error("Bad server response (timeout)");
                    processFailBusTicketPaymentStartRequest();
                }
            }));
        } else {
            log.warn("Empty selected sidebar menu item");
        }
    }

    private void processSuccessBusTicketPaymentStartRequest(BusTicketPaymentStartResponse response) {

        SideBarMenu.MenuItem menuItem = this.getSideBarMenu().getSelectedMenuItem();
        BusTicketsParamsWizardStep step = (BusTicketsParamsWizardStep) wizard.getWizardStep(BusTicketsParamsWizardStep.STEP_NO);
        if (step != null && (menuItem != null && menuItem.getData() instanceof String)) {
            step.setUp((String) menuItem.getData(), response.getDirections(), response.getRoutes(), response.getDates(), response.getBaggages());
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processFailBusTicketPaymentStartRequest() {
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("Receive bus tickets configuration params", "Bad receive bus tickets configuration params from server", Notification.Type.ERROR_MESSAGE);
    }

}
