/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.payway.commons.webapp.messaging.UIResponseCallBackImpl;
import com.payway.bustickets.airportexpress.ui.components.containers.ChoiceDtoBeanContainer;
import com.payway.bustickets.airportexpress.ui.components.containers.DirectionDtoBeanContainer;
import com.payway.bustickets.airportexpress.ui.components.containers.RouteDtoBeanContainer;
import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.bustickets.BusTicketValidateInvalidResponse;
import com.payway.messaging.message.bustickets.BusTicketValidateRequest;
import com.payway.messaging.message.bustickets.BusTicketValidateValidResponse;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BusTicketsWizard
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
public class BusTicketsWizard extends AbstractWizard {

    private static final long serialVersionUID = 7922687104437893528L;

    private static final int STEP_COUNT = 4;

    @UiField
    private Button btnLeft;
    @UiField
    private Button btnRight;

    @UiField
    private VerticalLayout layoutContent;

    @UiField
    private Image imgLogo;

    @Setter
    @Getter
    private MessageServerSenderService service;

    public BusTicketsWizard() {
        super(STEP_COUNT);
        init();
    }

    private void init() {

        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_airport_express_bus_tickets.png"));
        setContent(Clara.create("BusTicketsWizard.xml", this));

        getSteps().add(new BusTicketsParamsWizardStep());
        getSteps().add(new BusTicketsConfirmWizardStep());
        getSteps().add(new BusTicketsSuccessWizardStep());
        getSteps().add(new BusTicketsFailWizardStep());

        setStep(BusTicketsParamsWizardStep.STEP_NO);

        //
        btnLeft.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handleStepLeft();
                decorateStep();
            }
        });

        //
        btnRight.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handleStepRight();
                decorateStep();
            }
        });
    }

    private void decorateStep() {

        if (getStep() == BusTicketsParamsWizardStep.STEP_NO) { //begin            
            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 1 - Selection of parameters");

            btnLeft.setVisible(false);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Next");
        } else if (getStep() == BusTicketsConfirmWizardStep.STEP_NO) { //checkout  

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 2 - Confirmation and order");

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Checkout");

        } else if (getStep() == BusTicketsSuccessWizardStep.STEP_NO) { //success

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 3 - Successful order");

            btnLeft.setVisible(true);
            btnLeft.setCaption("New Buy");

            btnRight.setVisible(true);
            btnRight.setCaption("Save ticket");

        } else if (getStep() == BusTicketsFailWizardStep.STEP_NO) { //fail

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 3 - Failed order");

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("Try again");
        }
    }

    private void handleStepLeft() {

        if (BusTicketsConfirmWizardStep.STEP_NO == getStep()) {
            setStep(BusTicketsParamsWizardStep.STEP_NO);
        } else if (BusTicketsSuccessWizardStep.STEP_NO == getStep()) {
            setStep(BusTicketsParamsWizardStep.STEP_NO);
        }
    }

    private void handleStepRight() {

        if (BusTicketsParamsWizardStep.STEP_NO == getStep()) {
            processParams2ConfirmStep();
        } else if (BusTicketsConfirmWizardStep.STEP_NO == getStep()) {
            processConfirm2PayStep();
        } else if (BusTicketsFailWizardStep.STEP_NO == getStep()) {
            setStep(BusTicketsParamsWizardStep.STEP_NO);
        }
    }

    private void processParams2ConfirmStep() {

        BusTicketsParamsWizardStep wizardStep = (BusTicketsParamsWizardStep) getWizardStep(getStep());
        if (wizardStep != null) {
            if (wizardStep.validate()) {
                sendValidateRequest();
            } else {
                ((InteractionUI) UI.getCurrent()).showNotification("Validation bus ticket params", "Please, enter the correct values", Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    private void sendValidateRequest() {

        BusTicketsParamsWizardStep wizardStepParams = (BusTicketsParamsWizardStep) getWizardStep(BusTicketsParamsWizardStep.STEP_NO);

        if (wizardStepParams == null) {
            return;
        }

        String serviceProviderId = wizardStepParams.getOperatorId();
        String contactNumber = wizardStepParams.getEditContactNo().getValue();
        //String directionId = ((DirectionDtoBeanContainer) wizardStepParams.getCbDirection().getContainerDataSource()).getItem(wizardStepParams.getCbDirection().getValue()).getBean().getMnemonics();
        String dateId = ((ChoiceDtoBeanContainer) wizardStepParams.getCbTripDate().getContainerDataSource()).getItem(wizardStepParams.getCbTripDate().getValue()).getBean().getMnemonics();
        String routeId = ((RouteDtoBeanContainer) wizardStepParams.getCbRoute().getContainerDataSource()).getItem(wizardStepParams.getCbRoute().getValue()).getBean().getMnemonics();
        String baggageId = ((ChoiceDtoBeanContainer) wizardStepParams.getCbBaggage().getContainerDataSource()).getItem(wizardStepParams.getCbBaggage().getValue()).getBean().getMnemonics();
        int quantity = wizardStepParams.getSliderQuantity().getValue().intValue();

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new BusTicketValidateRequest(serviceProviderId, contactNumber, dateId, routeId, baggageId, quantity), new UIResponseCallBackImpl(getUI(), new UIResponseCallBackImpl.ResponseCallbackHandler() {
            @Override
            public void doServerResponse(final SuccessResponse response) {
                if (response instanceof BusTicketValidateValidResponse) {
                    processBusTicketValidateValidResponse((BusTicketValidateValidResponse) response);
                } else if (response instanceof BusTicketValidateInvalidResponse) {
                    processBusTicketValidateInvalidResponse((BusTicketValidateInvalidResponse) response);
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    processBusTicketValidateExceptionResponse();
                }
            }

            @Override
            public void doServerException(final ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                processBusTicketValidateExceptionResponse();
            }

            @Override
            public void doLocalException(final Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                processBusTicketValidateExceptionResponse();
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                processBusTicketValidateExceptionResponse();
            }
        }));
    }

    private void processBusTicketValidateValidResponse(BusTicketValidateValidResponse response) {

        BusTicketsParamsWizardStep wizardStepParams = (BusTicketsParamsWizardStep) getWizardStep(BusTicketsParamsWizardStep.STEP_NO);
        BusTicketsConfirmWizardStep wizardStepConfirm = (BusTicketsConfirmWizardStep) getWizardStep(BusTicketsConfirmWizardStep.STEP_NO);

        if (wizardStepParams != null && wizardStepConfirm != null) {
            setStep(BusTicketsConfirmWizardStep.STEP_NO);
            wizardStepConfirm.getEditContactNo().setValue(wizardStepParams.getEditContactNo().getValue());
            wizardStepConfirm.getEditDirection().setValue(((DirectionDtoBeanContainer) wizardStepParams.getCbDirection().getContainerDataSource()).getItem(wizardStepParams.getCbDirection().getValue()).getBean().getName());
            wizardStepConfirm.getEditRoute().setValue(((RouteDtoBeanContainer) wizardStepParams.getCbRoute().getContainerDataSource()).getItem(wizardStepParams.getCbRoute().getValue()).getBean().getDepartureTime());
            wizardStepConfirm.getEditTripDate().setValue(((ChoiceDtoBeanContainer) wizardStepParams.getCbTripDate().getContainerDataSource()).getItem(wizardStepParams.getCbTripDate().getValue()).getBean().getLabel());
            wizardStepConfirm.getEditBaggage().setValue(((ChoiceDtoBeanContainer) wizardStepParams.getCbBaggage().getContainerDataSource()).getItem(wizardStepParams.getCbBaggage().getValue()).getBean().getLabel());
            wizardStepConfirm.getEditQuantity().setValue(Integer.toString(wizardStepParams.getSliderQuantity().getValue().intValue()));
            wizardStepConfirm.getEditTotalCost().setValue(new DecimalFormat("###.##").format(response.getAmount()));
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processBusTicketValidateInvalidResponse(BusTicketValidateInvalidResponse response) {

        BusTicketsFailWizardStep wizardStep = (BusTicketsFailWizardStep) getWizardStep(BusTicketsFailWizardStep.STEP_NO);
        if (wizardStep != null) {
            wizardStep.getLbReason().setValue(response.getReason());
            setStep(BusTicketsFailWizardStep.STEP_NO);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processBusTicketValidateExceptionResponse() {

        BusTicketsFailWizardStep wizardStep = (BusTicketsFailWizardStep) getWizardStep(BusTicketsFailWizardStep.STEP_NO);
        if (wizardStep != null) {
            wizardStep.getLbReason().setValue("Internal server error");
            setStep(BusTicketsFailWizardStep.STEP_NO);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processConfirm2PayStep() {
        sendPayRequest();
    }

    private void sendPayRequest() {
        setStep(BusTicketsSuccessWizardStep.STEP_NO);
    }

    @Override
    public boolean setStep(int step) {

        if (super.setStep(step)) {
            decorateStep();
            return true;
        }

        return false;
    }

    @Override
    public void setLogoImage(final byte[] content) {

        final StreamResource streamResource = new StreamResource(
                new StreamResource.StreamSource() {
                    private static final long serialVersionUID = -2480723276190894707L;

                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(content);
                    }
                },
                "bus_tickets_wizard_logo.png"
        );

        streamResource.setCacheTime(0);

        imgLogo.setSource(streamResource);
    }
}
