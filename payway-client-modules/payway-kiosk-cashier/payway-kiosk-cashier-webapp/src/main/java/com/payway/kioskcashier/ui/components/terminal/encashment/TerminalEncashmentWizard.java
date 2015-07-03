/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractStandartButtonWizard;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.EncashmentReportFailureSearchRequest;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchRequest;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchResponse;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBoxListener;
import lombok.AccessLevel;
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

    public static final int STEP_COUNT = 5;

    public static final int TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID = 0;
    public static final int TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID = 1;
    public static final int TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID = 2;
    public static final int TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID = 3;
    public static final int TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID = 4;

    @Setter(AccessLevel.PRIVATE)
    boolean isHandleRightClick = true;

    public TerminalEncashmentWizard() {
        super(STEP_COUNT);
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_terminal_encashment.png"));
        setContent(Clara.create("TerminalEncashmentWizard.xml", this));

        setUpSteps();
        setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
    }

    private void setUpSteps() {
        getSteps().add(new SearchWizardStep());
        getSteps().add(new SearchFailWizardStep());
        getSteps().add(new CrudWizardStep());
        getSteps().add(new SuccessWizardStep());
        getSteps().add(new CrudFailWizardStep());
    }

    @Override
    protected void handleStepLeft() {
        
        if (TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
        } else if (TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        } else if (TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
        }
    }

    @UiHandler(value = "btnLeft")
    public void buttonClickLeft(Button.ClickEvent event) {

        if (!isHandleLeftClick()) {
            return;
        }

        handleStepLeft();
        decorateStep();
    }

    @UiHandler(value = "btnRight")
    public void buttonClickRight(Button.ClickEvent event) {

        if (!isHandleRightClick()) {
            return;
        }

        handleStepRight();
        decorateStep();
    }

    @Override
    protected void handleStepRight() {

        if (TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID == getStep()) {
            processSearchStep();
        } else if (TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID == getStep()) {
            ((AbstractUI) getUI()).showMessageBox("Save terminal encashment report", "Are you sure want to save terminal encashment report", Icon.QUESTION, new MessageBoxListener() {
                @Override
                public void buttonClicked(ButtonId buttonId) {
                    if (ButtonId.YES.equals(buttonId)) {
                        processCrudStep();
                        setHandleRightClick(true);
                    } else {
                        setHandleRightClick(false);
                    }
                }
            },
                    ButtonId.YES,
                    ButtonId.NO
            );

        } else if (TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID == getStep() || TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        } else if (TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID);
        }
    }

    @Override
    protected boolean isHandleRightClick() {
        return isHandleRightClick;
    }

    @Override
    protected void decorateStep() {

        if (getStep() == TERMINAL_ENCASHMENT_SEARCH_WIZARD_STEP_ID) { //begin   

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Search");

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("Search");

        } else if (getStep() == TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID) { //begin    

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Create");

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Save");

        } else if (getStep() == TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID) { //fail search

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Failed search");

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("New");

        } else if (getStep() == TERMINAL_ENCASHMENT_CRUD_FAIL_WIZARD_STEP_ID) { //fail crud

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Failed crud");

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("New");

        } else if (getStep() == TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID) { //success

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Successful");

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("New");
        }
    }

    @Override
    public boolean setStep(int step) {

        if (super.setStep(step)) {
            decorateStep();
            return true;
        }

        return false;
    }

    private void processCrudStep() {
        //
    }

    private void processSearchStep() {

        SearchWizardStep wizardStep = (SearchWizardStep) getWizardStep(getStep());
        if (wizardStep != null) {
            if (wizardStep.validate()) {
                sendSearchEncashmentReportRequest();
            } else {
                ((InteractionUI) UI.getCurrent()).showNotification("Validation search encashment report", "Please, enter the correct values", Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    private void sendSearchEncashmentReportRequest() {

        SearchWizardStep wizardStep = (SearchWizardStep) getWizardStep(0);
        if (wizardStep == null) {
            return;
        }

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new EncashmentReportSearchRequest(wizardStep.getEditTerminal().getValue(), Integer.parseInt(wizardStep.getEditReport().getValue())), new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {
                if (response instanceof EncashmentReportSearchResponse) {
                    processEncashmentReportSearchResponse((EncashmentReportSearchResponse) response);
                } else if (response instanceof EncashmentReportFailureSearchRequest) {
                    processEncashmentReportFailureSearchRequest((EncashmentReportFailureSearchRequest) response);
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
        if (wizardStep != null) {
            wizardStep.getLbReason().setValue("Internal server error");
            setStep(TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processEncashmentReportFailureSearchRequest(EncashmentReportFailureSearchRequest response) {

        SearchFailWizardStep wizardStep = (SearchFailWizardStep) getWizardStep(TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID);
        if (wizardStep != null) {
            wizardStep.getLbReason().setValue(response.getReason());
            setStep(TERMINAL_ENCASHMENT_SEARCH_FAIL_WIZARD_STEP_ID);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }

    private void processEncashmentReportSearchResponse(EncashmentReportSearchResponse response) {

        CrudWizardStep wizardStep = (CrudWizardStep) getWizardStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        if (wizardStep != null) {
            wizardStep.setUp(response);
            setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        }

        ((InteractionUI) UI.getCurrent()).closeProgressBar();
    }
}
