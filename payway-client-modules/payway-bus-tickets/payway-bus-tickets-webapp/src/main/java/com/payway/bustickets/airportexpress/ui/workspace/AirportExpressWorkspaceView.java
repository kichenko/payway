/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.workspace;

import com.payway.bustickets.airportexpress.ui.components.BusTicketsParamsWizardStep;
import com.payway.bustickets.airportexpress.ui.components.BusTicketsWizard;
import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.messaging.ResponseCallBack;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.bustickets.BusTicketPaymentStartRequest;
import com.payway.messaging.message.bustickets.BusTicketPaymentStartResponse;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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
@Component(value = "airport-express-bus-tickets-workspace-view")
public class AirportExpressWorkspaceView extends AbstractBusTicketsWorkspaceView {

    private static final long serialVersionUID = 4644909330724328135L;

    @UiField
    private BusTicketsWizard wizard;

    @Autowired
    @Qualifier("messageServerSenderService")
    private MessageServerSenderService service;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("AirportExpressWorkspaceView.xml", this));
    }

    @Override
    public void activate() {
        if (wizard.setStep(BusTicketsParamsWizardStep.STEP_NO)) {
            sendBusTicketPaymentStartRequest();
        }
    }

    private void sendBusTicketPaymentStartRequest() {

        ((InteractionUI) UI.getCurrent()).showProgressBar();

        SideBarMenu.MenuItem menuItem = this.getSideBarMenu().getSelectedMenuItem();
        if (menuItem != null && menuItem.getData() instanceof String) {

            service.sendMessage(new BusTicketPaymentStartRequest((String) menuItem.getData()), new ResponseCallBack() {

                @Override
                public void onServerResponse(final SuccessResponse response) {
                    final UI ui = getUI();
                    if (ui != null) {
                        ui.access(new Runnable() {
                            @Override
                            public void run() {
                                if (response instanceof BusTicketPaymentStartResponse) {
                                    processSuccessBusTicketPaymentStartRequest((BusTicketPaymentStartResponse) response);
                                } else {
                                    log.error("Bad server response (unknown type) - {}", response);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onServerException(final ExceptionResponse exception) {
                    final UI ui = getUI();
                    if (ui != null) {
                        ui.access(new Runnable() {
                            @Override
                            public void run() {
                                log.error("Bad server response (server exception) - {}", exception);
                                processFailBusTicketPaymentStartRequest();
                            }
                        });
                    }
                }

                @Override
                public void onLocalException(final Exception exception) {
                    log.error("Bad server response (local exception) - {}", exception);
                    processFailBusTicketPaymentStartRequest();
                }

                @Override
                public void onTimeout() {
                    log.error("Bad server response (timeout)");
                    processFailBusTicketPaymentStartRequest();
                }
            });
        } else {
            log.warn("Empty selected sidebar menu item");
        }
    }

    private void processSuccessBusTicketPaymentStartRequest(BusTicketPaymentStartResponse response) {

        BusTicketsParamsWizardStep step = (BusTicketsParamsWizardStep) wizard.getWizardStep(BusTicketsParamsWizardStep.STEP_NO);
        if (step != null) {
            step.setUp(response.getDirections(), response.getRoutes(), response.getDates(), response.getBaggages());
            //wizard.setStep(BusTicketsParamsWizardStep.getStepNo());
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processFailBusTicketPaymentStartRequest() {
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("Receive bus tickets configuration params", "Bad receive bus tickets configuration params from server", Notification.Type.ERROR_MESSAGE);
    }

}
