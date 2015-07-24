/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.terminal.encashment;

import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractStandartButtonWizard;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStandartButtonStep;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.commons.webapp.ui.components.wizard.FailWizardStep;
import com.payway.commons.webapp.ui.components.wizard.SuccessWizardStep;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveFailureResponse;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveResponse;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchFailureResponse;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBoxListener;
import java.util.List;
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

    public enum WizardStepType {

        Search(0),
        SearchFail(3),
        Crud(1),
        CrudFail(3),
        Discrepancy(2),
        DiscrepancyFail(3),
        Success(4);

        private final int viewIndex;

        private WizardStepType(int viewIndex) {
            this.viewIndex = viewIndex;
        }

        public int getViewIndex() {
            return viewIndex;
        }
    }

    public TerminalEncashmentWizard() {
        super(WizardStepType.values().length);
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_terminal_encashment.png"));

        setContent(Clara.create(getClass().getResourceAsStream("/com/payway/kioskcashier/ui/components/wizard/common/StandartButtonWizard.xml"), this));
        addActionHandler(new StandartButtonWizardKeyboardHandler());

        setUpSteps();
        setStep(WizardStepType.Search.ordinal());
        decorateStep();
    }

    private void setUpSteps() {
        getSteps().add(new SearchWizardStep());
        getSteps().add(new CrudWizardStep());
        getSteps().add(new CountingDiscrepancyWizardStep());
        getSteps().add(new FailWizardStep());
        getSteps().add(new SuccessWizardStep());
    }

    @Override
    protected int getCurrentViewIndex() {
        return WizardStepType.values()[getStep()].getViewIndex();
    }

    @Override
    public void setService(MessageServerSenderService value) {
        service = value;
        for (AbstractWizardStep ws : getSteps()) {
            ws.setService(value);
        }
    }

    @Override
    protected void handleStepLeft() {

        if (WizardStepType.SearchFail.ordinal() == getStep()) {
            setStep(WizardStepType.Search.ordinal());
        } else if (WizardStepType.CrudFail.ordinal() == getStep()) {
            setStep(WizardStepType.Crud.ordinal());
        } else if (WizardStepType.Crud.ordinal() == getStep()) {
            setStep(WizardStepType.Search.ordinal());
        } else if (WizardStepType.DiscrepancyFail.ordinal() == getStep()) {
            setStep(WizardStepType.Discrepancy.ordinal());
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

        if (WizardStepType.Search.ordinal() == getStep()) {
            try {
                getWizardStep(WizardStepType.values()[getStep()].getViewIndex()).validate();
                sendSearchEncashmentReportRequest();
            } catch (WizardStepValidationException ex) {
                ((InteractionUI) UI.getCurrent()).showNotification("Validation search encashment report", "Please, enter the correct values", Notification.Type.ERROR_MESSAGE);
            }
        } else if (WizardStepType.Crud.ordinal() == getStep()) {

            ((AbstractUI) getUI()).showMessageBox("Save encashment counting report", "Are you sure want to save report", Icon.QUESTION, new MessageBoxListener() {
                @Override
                public void buttonClicked(ButtonId buttonId) {
                    if (ButtonId.YES.equals(buttonId)) {

                        CrudWizardStep wizardStep = (CrudWizardStep) getWizardStep(WizardStepType.values()[getStep()].getViewIndex());
                        try {
                            wizardStep.validate();
                            sendEncashmentsResultRequest();
                        } catch (WizardStepValidationException ex) {
                            ((InteractionUI) UI.getCurrent()).showNotification("Validation encashments result", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                        }

                        decorateStep();
                    }
                }
            },
                    ButtonId.YES,
                    ButtonId.NO
            );
        } else if (WizardStepType.Success.ordinal() == getStep() || WizardStepType.CrudFail.ordinal() == getStep() || WizardStepType.DiscrepancyFail.ordinal() == getStep()) {
            setStep(WizardStepType.Search.ordinal());
            decorateStep();
        } else if (WizardStepType.SearchFail.ordinal() == getStep()) {
            setStep(WizardStepType.Search.ordinal());
            decorateStep();
        } else if (WizardStepType.Discrepancy.ordinal() == getStep()) {
            CountingDiscrepancyWizardStep wizardStep = (CountingDiscrepancyWizardStep) getWizardStep(WizardStepType.values()[getStep()].getViewIndex());
            try {
                wizardStep.validate();
                sendDiscrepancySaveRequest();
            } catch (WizardStepValidationException ex) {
                ((InteractionUI) UI.getCurrent()).showNotification("Validation encashments result", "Please, enter the correct values", Notification.Type.ERROR_MESSAGE);
            }

            decorateStep();
        }
    }

    @Override
    protected void decorateStep() {

        layoutContent.removeAllComponents();
        layoutContent.addComponent(getSteps().get(WizardStepType.values()[getStep()].getViewIndex()));

        if (getStep() == WizardStepType.Search.ordinal()) { //begin   
            setUpWizardControl("Search terminal encashment report", "", false, "Search", true);
        } else if (getStep() == WizardStepType.Crud.ordinal()) { //crud    
            setUpWizardControl("Create encashment counting report", "Back", true, "Save", true);
        } else if (getStep() == WizardStepType.SearchFail.ordinal()) { //fail search
            setUpWizardControl("Failed search terminal encashment report", "Back", true, "New", true);
        } else if (getStep() == WizardStepType.CrudFail.ordinal()) { //fail crud
            setUpWizardControl("Failed create encashment counting report", "Back", true, "New", true);
        } else if (getStep() == WizardStepType.Discrepancy.ordinal()) { //discrepancy
            setUpWizardControl("Create discrepancy encashment counting report", "", false, "Save", true);
        } else if (getStep() == WizardStepType.DiscrepancyFail.ordinal()) { //fail discrepancy
            setUpWizardControl("Failed create discrepancy encashment counting report", "Back", true, "New", true);
        } else if (getStep() == WizardStepType.Success.ordinal()) { //success
            setUpWizardControl("Encashment counting report successfully created", "", false, "New", true);
        }
    }

    private void sendDiscrepancySaveRequest() {

        CountingDiscrepancyWizardStep wizardStep = (CountingDiscrepancyWizardStep) getWizardStep(WizardStepType.values()[getStep()].getViewIndex());
        wizardStep.next(new AbstractWizardStandartButtonStep.ActionWizardStepHandler() {
            @Override
            public void success(Object... args) {
                SuccessWizardStep wizardStep = (SuccessWizardStep) getWizardStep(WizardStepType.Success.getViewIndex());
                wizardStep.getLbMessage().setValue("Succefull saved");
                setStep(WizardStepType.Success.ordinal());
            }

            @Override
            public void failure(Object... args) {
                //
            }

            @Override
            public void exception(Exception ex) {
                FailWizardStep wizardStep = (FailWizardStep) getWizardStep(WizardStepType.DiscrepancyFail.getViewIndex());
                wizardStep.getLbReason().setValue("Internal server error");
                setStep(WizardStepType.DiscrepancyFail.ordinal());

            }
        });
    }

    private void sendEncashmentsResultRequest() {

        CrudWizardStep wizardStep = (CrudWizardStep) getWizardStep(WizardStepType.values()[getStep()].getViewIndex());
        wizardStep.next(new AbstractWizardStandartButtonStep.ActionWizardStepHandler() {

            @Override
            public void success(Object... args) {
                EncashmentCountingSaveResponse response = (EncashmentCountingSaveResponse) args[0];
                if (response.getShortage() != 0 || response.getSurplus() != 0) {
                    CountingDiscrepancyWizardStep wizardStep = (CountingDiscrepancyWizardStep) getWizardStep(WizardStepType.Discrepancy.getViewIndex());
                    wizardStep.setupStep(new CountingDiscrepancyWizardStep.CountingDiscrepancyWizardStepParams(response.getCountingId(), getCurrency(), response.getSurplus(), response.getShortage()));
                    setStep(WizardStepType.Discrepancy.ordinal());
                } else {
                    SuccessWizardStep wizardStep = (SuccessWizardStep) getWizardStep(WizardStepType.Success.getViewIndex());
                    wizardStep.getLbMessage().setValue("No surplus or shortage found");
                    setStep(WizardStepType.Success.ordinal());
                }
            }

            @Override
            public void failure(Object... args) {
                FailWizardStep wizardStep = (FailWizardStep) getWizardStep(WizardStepType.CrudFail.getViewIndex());
                wizardStep.getLbReason().setValue(((EncashmentCountingSaveFailureResponse) args[0]).getReason());
                setStep(WizardStepType.CrudFail.ordinal());
            }

            @Override
            public void exception(Exception ex) {
                FailWizardStep wizardStep = (FailWizardStep) getWizardStep(WizardStepType.CrudFail.getViewIndex());
                wizardStep.getLbReason().setValue("Internal server error");
                setStep(WizardStepType.CrudFail.ordinal());
            }
        }, getSessionId());
    }

    private void sendSearchEncashmentReportRequest() {

        SearchWizardStep wizardStep = (SearchWizardStep) getWizardStep(WizardStepType.Search.getViewIndex());
        wizardStep.next(new AbstractWizardStandartButtonStep.ActionWizardStepHandler() {

            @Override
            public void success(Object... args) {
                CrudWizardStep wizardStep = (CrudWizardStep) getWizardStep(WizardStepType.Crud.getViewIndex());
                wizardStep.setupStep(new CrudWizardStep.CrudWizardStepWizardStepParams((KioskEncashmentDto) args[0], (List<BanknoteNominalDto>) args[1], getCurrency()));
                setStep(WizardStepType.Crud.ordinal());
            }

            @Override
            public void failure(Object... args) {
                FailWizardStep wizardStep = (FailWizardStep) getWizardStep(WizardStepType.SearchFail.getViewIndex());
                wizardStep.getLbReason().setValue(((EncashmentReportSearchFailureResponse) args[0]).getReason());
                setStep(WizardStepType.SearchFail.ordinal());
            }

            @Override
            public void exception(Exception ex) {
                FailWizardStep wizardStep = (FailWizardStep) getWizardStep(WizardStepType.SearchFail.getViewIndex());
                wizardStep.getLbReason().setValue("Internal server error");
                setStep(WizardStepType.SearchFail.ordinal());
            }
        }, getSessionId());
    }
}
