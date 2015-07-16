/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractStandartButtonWizard;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.EncashmentCountingDiscrepancySaveRequest;
import com.payway.messaging.message.kioskcashier.EncashmentCountingDiscrepancySaveResponse;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveFailureResponse;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveRequest;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveResponse;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchFailureResponse;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchRequest;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBoxListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * TerminalEncashmentWizard
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Slf4j
public final class TerminalEncashmentWizard extends AbstractStandartButtonWizard {

    private static final long serialVersionUID = -4237687965034396262L;

    public static final int STEP_COUNT = 7;

    public static final int TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID = 0;
    public static final int TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID = 1;
    public static final int TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID = 2;
    public static final int TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID = 3;
    public static final int TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_WIZARD_STEP_ID = 4;
    public static final int TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_FAIL_WIZARD_STEP_ID = 5;
    public static final int TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID = 6;

    @Setter
    @Getter
    private CurrencyDto currency;

    @Setter
    @Getter
    private String sessionId;

    public TerminalEncashmentWizard() {
        super(STEP_COUNT);
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_terminal_encashment.png"));
        setContent(Clara.create("TerminalEncashmentWizard.xml", this));
        addActionHandler(new StandartButtonWizardKeyboardHandler());

        setUpSteps();
        setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
        decorateStep();
    }

    private void setUpSteps() {

        getSteps().add(new SearchWizardStep());
        getSteps().add(new SearchFailWizardStep());
        getSteps().add(new CrudWizardStep());
        getSteps().add(new CrudFailWizardStep());
        getSteps().add(new CountingDiscrepancyWizardStep());
        getSteps().add(new CountingDiscrepancyFailWizardStep());
        getSteps().add(new SuccessWizardStep());
    }

    @Override
    protected void handleStepLeft() {

        if (TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
        } else if (TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        } else if (TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
        } else if (TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_WIZARD_STEP_ID);
        }
    }

    @UiHandler(value = "btnLeft")
    public void buttonClickLeft(Button.ClickEvent event) {
        handleStepLeft();
        decorateStep();
    }

    @UiHandler(value = "btnRight")
    public void buttonClickRight(Button.ClickEvent event) {
        handleStepRight();
    }

    @Override
    protected void handleStepRight() {

        if (TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID == getStep()) {
            processSearchStep();
        } else if (TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID == getStep()) {

            ((AbstractUI) getUI()).showMessageBox("Save encashment counting report", "Are you sure want to save report", Icon.QUESTION, new MessageBoxListener() {
                @Override
                public void buttonClicked(ButtonId buttonId) {
                    if (ButtonId.YES.equals(buttonId)) {
                        processCrudStep();
                        decorateStep();
                    }
                }
            },
                    ButtonId.YES,
                    ButtonId.NO
            );
        } else if (TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID == getStep() || TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID == getStep() || TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
            decorateStep();
        } else if (TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
            decorateStep();
        } else if (TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_WIZARD_STEP_ID == getStep()) {
            processDiscrepancy();
            decorateStep();
        }
    }

    @Override
    protected void decorateStep() {

        layoutContent.removeAllComponents();
        layoutContent.addComponent(getSteps().get(getStep()));

        if (getStep() == TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID) { //begin   
            setUpWizardControl("Search terminal encashment report", "", false, "Search", true);
        } else if (getStep() == TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID) { //crud    
            setUpWizardControl("Create encashment counting report", "Back", true, "Save", true);
        } else if (getStep() == TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID) { //fail search
            setUpWizardControl("Failed search terminal encashment report", "Back", true, "New", true);
        } else if (getStep() == TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID) { //fail crud
            setUpWizardControl("Failed create encashment counting report", "Back", true, "New", true);
        } else if (getStep() == TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_WIZARD_STEP_ID) { //discrepancy
            setUpWizardControl("Create discrepancy encashment counting report", "", false, "Save", true);
        } else if (getStep() == TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_FAIL_WIZARD_STEP_ID) { //fail discrepancy
            setUpWizardControl("Failed create discrepancy encashment counting report", "Back", true, "New", true);
        } else if (getStep() == TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID) { //success
            setUpWizardControl("Encashment counting report successfully created", "", false, "New", true);
        }
    }

    private void processCrudStep() {

        CrudWizardStep wizardStep = (CrudWizardStep) getWizardStep(getStep());
        try {
            wizardStep.validate();
            sendEncashmentsResultRequest();
        } catch (WizardStepValidationException ex) {
            ((InteractionUI) UI.getCurrent()).showNotification("Validation encashments result", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void processDiscrepancy() {

        CountingDiscrepancyWizardStep wizardStep = (CountingDiscrepancyWizardStep) getWizardStep(getStep());
        try {
            wizardStep.validate();
            sendDiscrepancySaveRequest();
        } catch (WizardStepValidationException ex) {
            ((InteractionUI) UI.getCurrent()).showNotification("Validation encashments result", "Please, enter the correct values", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void sendDiscrepancySaveRequest() {

        CountingDiscrepancyWizardStep wizardStep = (CountingDiscrepancyWizardStep) getWizardStep(getStep());
        CountingDiscrepancyWizardStep.CountingDiscrepancyWizardStepState state = wizardStep.getStepState();

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new EncashmentCountingDiscrepancySaveRequest(state.getCountingId(), state.getDiscrepancies()),
                new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

                    @Override
                    public void doServerResponse(SuccessResponse response) {
                        if (response instanceof EncashmentCountingDiscrepancySaveResponse) {
                            processEncashmentCountingDiscrepancySaveResponse();
                        } else {
                            log.error("Bad server response (unknown type) - {}", response);
                            processDiscrepancySaveExceptionResponse();
                        }
                    }

                    @Override
                    public void doServerException(ExceptionResponse exception) {
                        log.error("Bad exception response (server exception) - {}", exception);
                        processDiscrepancySaveExceptionResponse();
                    }

                    @Override
                    public void doLocalException(Exception exception) {
                        log.error("Bad exception response (local exception) - {}", exception);
                        processDiscrepancySaveExceptionResponse();
                    }

                    @Override
                    public void doTimeout() {
                        log.error("Bad exception response (time out)");
                        processDiscrepancySaveExceptionResponse();
                    }
                })
        );
    }

    private void processEncashmentCountingDiscrepancySaveResponse() {

        SuccessWizardStep wizardStep = (SuccessWizardStep) getWizardStep(TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID);
        wizardStep.getLbMessage().setValue("Succefull saved");
        setStep(TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID);

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processDiscrepancySaveExceptionResponse() {

        CountingDiscrepancyFailWizardStep wizardStep = (CountingDiscrepancyFailWizardStep) getWizardStep(TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_FAIL_WIZARD_STEP_ID);
        wizardStep.getLbReason().setValue("Internal server error");
        setStep(TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_FAIL_WIZARD_STEP_ID);

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processSearchStep() {

        SearchWizardStep wizardStep = (SearchWizardStep) getWizardStep(getStep());
        try {
            wizardStep.validate();
            sendSearchEncashmentReportRequest();
        } catch (WizardStepValidationException ex) {
            ((InteractionUI) UI.getCurrent()).showNotification("Validation search encashment report", "Please, enter the correct values", Notification.Type.ERROR_MESSAGE);
        }
    }

    private void sendEncashmentsResultRequest() {

        CrudWizardStep wizardStep = (CrudWizardStep) getWizardStep(getStep());

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new EncashmentCountingSaveRequest(getSessionId(), wizardStep.getKioskEncashment().getId(), wizardStep.getEncashments()), new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {

                if (response instanceof EncashmentCountingSaveResponse) {
                    processEncashmentCountingSaveResponse((EncashmentCountingSaveResponse) response);
                } else if (response instanceof EncashmentCountingSaveFailureResponse) {
                    processEncashmentCountingSaveFailureResponse((EncashmentCountingSaveFailureResponse) response);
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    processEncashmentCountingSaveExceptionResponse();
                }
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                processEncashmentCountingSaveExceptionResponse();
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                processEncashmentCountingSaveExceptionResponse();
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                processEncashmentCountingSaveExceptionResponse();
            }
        }));
    }

    private void processEncashmentCountingSaveResponse(EncashmentCountingSaveResponse response) {

        if (response.getShortage() != 0 || response.getSurplus() != 0) {
            CountingDiscrepancyWizardStep wizardStep = (CountingDiscrepancyWizardStep) getWizardStep(TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_WIZARD_STEP_ID);
            wizardStep.setupStep(new CountingDiscrepancyWizardStep.CountingDiscrepancyWizardStepParams(response.getCountingId(), getCurrency(), response.getSurplus(), response.getShortage()));
            setStep(TERMINAL_ENCASHMENT_COUNTING_DISCREPANCY_WIZARD_STEP_ID);
        } else {
            SuccessWizardStep wizardStep = (SuccessWizardStep) getWizardStep(TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID);
            wizardStep.setUp();
            setStep(TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processEncashmentCountingSaveFailureResponse(EncashmentCountingSaveFailureResponse response) {

        CrudFailWizardStep wizardStep = (CrudFailWizardStep) getWizardStep(TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID);
        wizardStep.getLbReason().setValue(response.getReason());
        setStep(TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID);

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processEncashmentCountingSaveExceptionResponse() {

        CrudFailWizardStep wizardStep = (CrudFailWizardStep) getWizardStep(TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID);
        wizardStep.getLbReason().setValue("Internal server error");
        setStep(TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID);

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void sendSearchEncashmentReportRequest() {

        SearchWizardStep wizardStep = (SearchWizardStep) getWizardStep(0);
        SearchWizardStep.SearchWizardStepState state = wizardStep.getStepState();

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new EncashmentReportSearchRequest(getSessionId(), state.getTerminalName(), state.getReportNo()), new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {
                if (response instanceof EncashmentReportSearchResponse) {
                    processEncashmentReportSearchResponse((EncashmentReportSearchResponse) response);
                } else if (response instanceof EncashmentReportSearchFailureResponse) {
                    processEncashmentReportFailureSearchRequest((EncashmentReportSearchFailureResponse) response);
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    processSearchEncashmentReportExceptionResponse();
                }
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                processSearchEncashmentReportExceptionResponse();
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                processSearchEncashmentReportExceptionResponse();
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                processSearchEncashmentReportExceptionResponse();
            }
        }));
    }

    private void processSearchEncashmentReportExceptionResponse() {

        SearchFailWizardStep wizardStep = (SearchFailWizardStep) getWizardStep(TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID);
        wizardStep.getLbReason().setValue("Internal server error");
        setStep(TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID);

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processEncashmentReportFailureSearchRequest(EncashmentReportSearchFailureResponse response) {

        SearchFailWizardStep wizardStep = (SearchFailWizardStep) getWizardStep(TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID);
        wizardStep.getLbReason().setValue(response.getReason());
        setStep(TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID);

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processEncashmentReportSearchResponse(EncashmentReportSearchResponse response) {

        CrudWizardStep wizardStep = (CrudWizardStep) getWizardStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        wizardStep.setUp(response, getCurrency());
        setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }
}
