/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.payway.commons.webapp.messaging.UIResponseCallBackImpl;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.bustickets.BusTicketPaymentStartRequest;
import com.payway.messaging.message.bustickets.BusTicketPaymentStartResponse;
import com.payway.messaging.message.bustickets.BusTicketPurchaseRequest;
import com.payway.messaging.message.bustickets.BusTicketPurchaseResponse;
import com.payway.messaging.message.bustickets.BusTicketValidateInvalidResponse;
import com.payway.messaging.message.bustickets.BusTicketValidateRequest;
import com.payway.messaging.message.bustickets.BusTicketValidateValidResponse;
import com.payway.messaging.message.common.TransactionReceiptRequest;
import com.payway.messaging.message.common.TransactionReceiptResponse;
import com.payway.messaging.model.common.RetailerTerminalDto;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
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

    public static final int STEP_COUNT = 4;

    public static final int BUS_TICKETS_PARAMS_WIZARD_STEP_ID = 0;
    public static final int BUS_TICKETS_CONFIRM_WIZARD_STEP_ID = 1;
    public static final int BUS_TICKETS_SUCCESS_WIZARD_STEP_ID = 2;
    public static final int BUS_TICKETS_FAIL_WIZARD_STEP_ID = 3;

    @UiField
    private Button btnLeft;

    @UiField
    private Button btnRight;

    @UiField
    private VerticalLayout layoutContent;

    @UiField
    private Image imgLogo;

    @UiField
    private RetailerTerminalPanel retailerTerminalPanel;

    @Getter
    @Setter
    private String sessionId;

    @Getter
    @Setter
    private String operatorId;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private double totalCost;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Date paymentStart;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Date paymentStop;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private long checkId;

    public BusTicketsWizard() {
        super(STEP_COUNT);
        init();
    }

    private void init() {

        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_airport_express_bus_tickets.png"));
        setContent(Clara.create("BusTicketsWizard.xml", this));

        setUpSteps();
        setStep(BUS_TICKETS_PARAMS_WIZARD_STEP_ID);

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

        setTotalCost(0);
        setPaymentStart(null);
        setPaymentStop(null);
    }

    private void setUpSteps() {
        getSteps().add(new BusTicketsParamsWizardStep());
        getSteps().add(new BusTicketsConfirmWizardStep());
        getSteps().add(new BusTicketsSuccessWizardStep());
        getSteps().add(new BusTicketsFailWizardStep());
    }

    private void decorateStep() {

        if (getStep() == BUS_TICKETS_PARAMS_WIZARD_STEP_ID) { //begin            
            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 1 - Selection of parameters");
            retailerTerminalPanel.setEnabled(true);

            btnLeft.setVisible(false);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Next");
        } else if (getStep() == BUS_TICKETS_CONFIRM_WIZARD_STEP_ID) { //checkout  

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 2 - Confirmation and order");
            retailerTerminalPanel.setEnabled(false);

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Checkout");

        } else if (getStep() == BUS_TICKETS_SUCCESS_WIZARD_STEP_ID) { //success

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 3 - Successful order");
            retailerTerminalPanel.setEnabled(false);

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("New Buy");

        } else if (getStep() == BUS_TICKETS_FAIL_WIZARD_STEP_ID) { //fail

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 3 - Failed order");
            retailerTerminalPanel.setEnabled(false);

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("Try again");
        }
    }

    private void handleStepLeft() {

        if (BUS_TICKETS_CONFIRM_WIZARD_STEP_ID == getStep()) {
            setStep(BUS_TICKETS_PARAMS_WIZARD_STEP_ID);
        } else if (BUS_TICKETS_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(BUS_TICKETS_PARAMS_WIZARD_STEP_ID);
        }
    }

    private void handleStepRight() {

        if (BUS_TICKETS_PARAMS_WIZARD_STEP_ID == getStep()) {
            processParams2ConfirmStep();
        } else if (BUS_TICKETS_CONFIRM_WIZARD_STEP_ID == getStep()) {
            processConfirmToPurchase();
        } else if (BUS_TICKETS_FAIL_WIZARD_STEP_ID == getStep() || BUS_TICKETS_SUCCESS_WIZARD_STEP_ID == getStep()) {
            setStep(BUS_TICKETS_PARAMS_WIZARD_STEP_ID);
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

    private void processConfirmToPurchase() {
        sendPurchaseRequest();
    }

    private void sendPurchaseRequest() {

        BusTicketsParamsWizardStep wizardStepParams = (BusTicketsParamsWizardStep) getWizardStep(BUS_TICKETS_PARAMS_WIZARD_STEP_ID);
        if (wizardStepParams == null) {
            return;
        }

        setPaymentStop(new WebBrowser().getCurrentDate());

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new BusTicketPurchaseRequest(UUID.randomUUID().toString(), getSessionId(), retailerTerminalPanel.getRetailerTerminalId(), getOperatorId(), wizardStepParams.getContactNo(), wizardStepParams.getTripDate().getMnemonics(), wizardStepParams.getRoute().getMnemonics(), wizardStepParams.getBaggage().getMnemonics(), wizardStepParams.getQuantity(), getPaymentStart(), getPaymentStop(), getTotalCost()), new UIResponseCallBackImpl(getUI(), new UIResponseCallBackImpl.ResponseCallbackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {
                if (response instanceof BusTicketPurchaseResponse) {
                    processBusTicketPurchaseResponse((BusTicketPurchaseResponse) response);
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    processBusTicketExceptionResponse();
                }
            }

            @Override
            public void doServerException(final ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                processBusTicketExceptionResponse();
            }

            @Override
            public void doLocalException(final Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                processBusTicketExceptionResponse();
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                processBusTicketExceptionResponse();
            }
        }));
    }

    private void processBusTicketPurchaseResponse(BusTicketPurchaseResponse response) {
        setCheckId(response.getTxId());
        setStep(BUS_TICKETS_SUCCESS_WIZARD_STEP_ID);
        sendDownloadCheckRequest();
    }

    private void sendDownloadCheckRequest() {

        getService().sendMessage(new TransactionReceiptRequest(getSessionId(), getCheckId()), new UIResponseCallBackImpl(getUI(), new UIResponseCallBackImpl.ResponseCallbackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {

                if (response instanceof TransactionReceiptResponse) {
                    TransactionReceiptResponse rsp = (TransactionReceiptResponse) response;
                    if (rsp.getContent() != null) {
                        processDownloadCheckRequest((TransactionReceiptResponse) response);
                    } else {
                        log.error("Bad server response (content is empty)");
                        processBusTicketCheckDownloadExceptionResponse();
                    }
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    processBusTicketCheckDownloadExceptionResponse();
                }
            }

            @Override
            public void doServerException(final ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                processBusTicketCheckDownloadExceptionResponse();
            }

            @Override
            public void doLocalException(final Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                processBusTicketCheckDownloadExceptionResponse();
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                processBusTicketCheckDownloadExceptionResponse();
            }
        }));
    }

    private void processDownloadCheckRequest(final TransactionReceiptResponse response) {

        try {
            FileOutputStream st = new FileOutputStream(new File("c://users//sergey//1.pdf"));
            st.write(response.getContent());
            st.close();
        } catch (Exception ex) {
            int k = 900;
        }

        final StreamResource streamResource = new StreamResource(
                new StreamResource.StreamSource() {
                    private static final long serialVersionUID = -2480723276190894707L;

                    @Override
                    public InputStream getStream() {
                        return new ByteArrayInputStream(response.getContent());
                    }
                },
                response.getLabel()
        );

        streamResource.setMIMEType(response.getContentType());

        BusTicketsSuccessWizardStep stepWizard = (BusTicketsSuccessWizardStep) getWizardStep(BUS_TICKETS_SUCCESS_WIZARD_STEP_ID);
        if (stepWizard != null) {
            stepWizard.setSource(streamResource);
        } else {
            log.warn("Bad step wizard step");
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processBusTicketCheckDownloadExceptionResponse() {
        ((InteractionUI) UI.getCurrent()).closeProgressBar();
        ((InteractionUI) UI.getCurrent()).showNotification("Download bus ticket check", "Bad downloading bus ticket check...", Notification.Type.ERROR_MESSAGE);
    }

    private void sendValidateRequest() {

        BusTicketsParamsWizardStep wizardStepParams = (BusTicketsParamsWizardStep) getWizardStep(BUS_TICKETS_PARAMS_WIZARD_STEP_ID);

        if (wizardStepParams == null) {
            return;
        }

        ((InteractionUI) UI.getCurrent()).showProgressBar();

        getService().sendMessage(new BusTicketValidateRequest(getSessionId(), retailerTerminalPanel.getRetailerTerminalId(), getOperatorId(), wizardStepParams.getContactNo(), wizardStepParams.getTripDate().getMnemonics(), wizardStepParams.getRoute().getMnemonics(), wizardStepParams.getBaggage().getMnemonics(), wizardStepParams.getQuantity()), new UIResponseCallBackImpl(getUI(), new UIResponseCallBackImpl.ResponseCallbackHandler() {
            @Override
            public void doServerResponse(final SuccessResponse response) {
                if (response instanceof BusTicketValidateValidResponse) {
                    processBusTicketValidateValidResponse((BusTicketValidateValidResponse) response);
                } else if (response instanceof BusTicketValidateInvalidResponse) {
                    processBusTicketValidateInvalidResponse((BusTicketValidateInvalidResponse) response);
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    processBusTicketExceptionResponse();
                }
            }

            @Override
            public void doServerException(final ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                processBusTicketExceptionResponse();
            }

            @Override
            public void doLocalException(final Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                processBusTicketExceptionResponse();
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                processBusTicketExceptionResponse();
            }
        }));
    }

    private void processBusTicketValidateValidResponse(BusTicketValidateValidResponse response) {

        BusTicketsParamsWizardStep wizardStepParams = (BusTicketsParamsWizardStep) getWizardStep(BUS_TICKETS_PARAMS_WIZARD_STEP_ID);
        BusTicketsConfirmWizardStep wizardStepConfirm = (BusTicketsConfirmWizardStep) getWizardStep(BUS_TICKETS_CONFIRM_WIZARD_STEP_ID);

        try {
            if (wizardStepParams != null && wizardStepConfirm != null) {

                //set total price: [TotalPrice=Price*Quantity] else [TotalPrice=Amount]
                if (response.getAmount() == null) {
                    setTotalCost(wizardStepParams.getRoute().getPrice() * wizardStepParams.getQuantity());
                } else {
                    setTotalCost(response.getAmount());
                }

                setStep(BUS_TICKETS_CONFIRM_WIZARD_STEP_ID);
                wizardStepConfirm.setDirection(wizardStepParams.getDirection().getName());
                wizardStepConfirm.setRoute(wizardStepParams.getRoute().getDepartureTime());
                wizardStepConfirm.setTripDate(wizardStepParams.getTripDate().getLabel());
                wizardStepConfirm.setBaggage(wizardStepParams.getBaggage().getLabel());
                wizardStepConfirm.setContactNo(wizardStepParams.getContactNo());
                wizardStepConfirm.setQuantity(wizardStepParams.getQuantity());
                wizardStepConfirm.setRouteName(response.getRouteName());
                wizardStepConfirm.setHasDiscount(response.getAmount() != null);
                wizardStepConfirm.setTotalCost(getTotalCost());
            }
        } finally {
            ((InteractionUI) UI.getCurrent()).closeProgressBar();
        }
    }

    private void processBusTicketValidateInvalidResponse(BusTicketValidateInvalidResponse response) {

        BusTicketsFailWizardStep wizardStep = (BusTicketsFailWizardStep) getWizardStep(BUS_TICKETS_FAIL_WIZARD_STEP_ID);
        if (wizardStep != null) {
            wizardStep.getLbReason().setValue(response.getReason());
            setStep(BUS_TICKETS_FAIL_WIZARD_STEP_ID);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processBusTicketExceptionResponse() {

        BusTicketsFailWizardStep wizardStep = (BusTicketsFailWizardStep) getWizardStep(BUS_TICKETS_FAIL_WIZARD_STEP_ID);
        if (wizardStep != null) {
            wizardStep.getLbReason().setValue("Internal server error");
            setStep(BUS_TICKETS_FAIL_WIZARD_STEP_ID);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
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
                "bus_tickets_wizard_logo_" + getOperatorId() + ".png"
        );

        streamResource.setCacheTime(0);

        imgLogo.setSource(streamResource);
    }

    public void setUpTerminals(List<RetailerTerminalDto> terminals) {
        retailerTerminalPanel.setUpTerminals(terminals);
    }

    public void setUpBusTicketsPaymentParams() {

        setPaymentStart(new WebBrowser().getCurrentDate());

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new BusTicketPaymentStartRequest(getOperatorId()), new UIResponseCallBackImpl(getUI(), new UIResponseCallBackImpl.ResponseCallbackHandler() {

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
                processBusTicketExceptionResponse();
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad server response (local exception) - {}", exception);
                processBusTicketExceptionResponse();
            }

            @Override
            public void doTimeout() {
                log.error("Bad server response (timeout)");
                processBusTicketExceptionResponse();
            }
        }));
    }

    private void processSuccessBusTicketPaymentStartRequest(BusTicketPaymentStartResponse response) {

        BusTicketsParamsWizardStep wizardStep = (BusTicketsParamsWizardStep) getWizardStep(BusTicketsWizard.BUS_TICKETS_PARAMS_WIZARD_STEP_ID);
        if (wizardStep != null) {
            wizardStep.setUp(response.getDirections(), response.getRoutes(), response.getDates(), response.getBaggages());
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }
}
