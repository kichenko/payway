/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit;

import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractStandartButtonWizard;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStandartButtonStep;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.commons.webapp.ui.components.wizard.FailWizardStep;
import com.payway.commons.webapp.ui.components.wizard.SuccessWizardStep;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BankCashDepositModel;
import com.payway.messaging.model.common.BankAccountDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * BankCashDepositWizard
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Slf4j
public final class BankCashDepositWizard extends AbstractStandartButtonWizard {

    private static final long serialVersionUID = -6061678685975286200L;

    public enum WizardStepType {

        Deposit(0),
        Nominal(1),
        Discrepancy(2),
        Success(3),
        Fail(4);

        private final int viewIndex;

        private WizardStepType(int viewIndex) {
            this.viewIndex = viewIndex;
        }

        public int getViewIndex() {
            return viewIndex;
        }
    }

    @Getter
    @Setter
    private BankAccountDto accountCashDeposit;

    private final BeanItem<BankCashDepositModel> beanItem = new BeanItem<>(new BankCashDepositModel());

    public BankCashDepositWizard() {
        super(WizardStepType.values().length);
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_bank_cash_deposit.png"));
        setContent(Clara.create(getClass().getResourceAsStream("/com/payway/kioskcashier/ui/components/wizard/common/StandartButtonWizard.xml"), this));
        addActionHandler(new StandartButtonWizardKeyboardHandler());

        setUpSteps();
        setStep(WizardStepType.Deposit.ordinal());
        decorateStep();
    }

    private void setUpSteps() {

        getSteps().add(new CreateBankCashDepositWizardStep());
        getSteps().add(new AddNominalsBankCashDepositWizardStep());
        getSteps().add(new DiscrepancyBankCashDepositWizardStep());
        getSteps().add(new SuccessWizardStep());
        getSteps().add(new FailWizardStep());

        for (AbstractWizardStep ws : getSteps()) {
            if (ws instanceof BeanItemWizardStep) {
                ((BeanItemWizardStep) ws).setBeanItem(beanItem);
            }
        }
    }

    @Override
    public void activate() {
        ((AbstractWizardStandartButtonStep) getCurrentWizardStep()).setupStep(new CreateBankCashDepositWizardStep.CreateBankCashDepositWizardStepParams(getAccountCashDeposit()));
    }

    @Override
    public void refresh() {
        ((CreateBankCashDepositWizardStep) getSteps().get(WizardStepType.Deposit.getViewIndex())).refreshStep(new CreateBankCashDepositWizardStep.CreateBankCashDepositWizardStepParams(accountCashDeposit));
    }

    @Override
    public void setService(MessageServerSenderService value) {
        service = value;
        for (AbstractWizardStep ws : getSteps()) {
            ws.setService(value);
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
    protected void handleStepLeft() {

        if (WizardStepType.Nominal.ordinal() == getStep()) {
            setStep(WizardStepType.Deposit.ordinal());
        } else if (WizardStepType.Discrepancy.ordinal() == getStep()) {
            setStep(WizardStepType.Nominal.ordinal());
        } else if (WizardStepType.Fail.ordinal() == getStep()) {
            setStep(WizardStepType.Discrepancy.ordinal());
        }
    }

    @Override
    protected int getCurrentViewIndex() {
        return WizardStepType.values()[getStep()].getViewIndex();
    }

    @Override
    protected void handleStepRight() {

        if (WizardStepType.Deposit.ordinal() == getStep()) {
            try {
                getCurrentWizardStep().validate();
                ((AbstractWizardStandartButtonStep) getCurrentWizardStep()).next(new AbstractWizardStandartButtonStep.ActionWizardStepHandler() {

                    @Override
                    public void success(Object... args) {
                        setStep(WizardStepType.Nominal.ordinal());
                        getCurrentWizardStep().setupStep(new AddNominalsBankCashDepositWizardStep.AddNominalsBankCashDepositWizardStepParams(getCurrency(), (List<BanknoteNominalDto>) args[0]));
                    }

                    @Override
                    public void failure(Object... args) {
                        //
                    }

                    @Override
                    public void exception(Exception ex) {
                        //
                    }
                });
            } catch (WizardStepValidationException ex) {
                ((InteractionUI) UI.getCurrent()).showNotification("Validation cash deposit", "Please, enter the correct values", Notification.Type.ERROR_MESSAGE);
            }
        } else if (WizardStepType.Nominal.ordinal() == getStep()) {
            try {
                getCurrentWizardStep().validate();
                ((AbstractWizardStandartButtonStep) getCurrentWizardStep()).next(new AbstractWizardStandartButtonStep.ActionWizardStepHandler() {

                    @Override
                    public void success(Object... args) {
                        setStep(WizardStepType.Discrepancy.ordinal());
                        getCurrentWizardStep().setupStep(new DiscrepancyBankCashDepositWizardStep.DiscrepancyBankCashDepositWizardStepParams(getCurrency()));
                    }

                    @Override
                    public void failure(Object... args) {
                        //
                    }

                    @Override
                    public void exception(Exception ex) {
                        //
                    }
                });
            } catch (WizardStepValidationException ex) {
                ((InteractionUI) UI.getCurrent()).showNotification("Validation cash deposit", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        } else if (WizardStepType.Discrepancy.ordinal() == getStep()) {
            try {
                getCurrentWizardStep().validate();
                ((AbstractWizardStandartButtonStep) getCurrentWizardStep()).next(new AbstractWizardStandartButtonStep.ActionWizardStepHandler() {

                    @Override
                    public void success(Object... args) {
                        setStep(WizardStepType.Success.ordinal());
                    }

                    @Override
                    public void failure(Object... args) {
                        //
                    }

                    @Override
                    public void exception(Exception ex) {
                        //
                    }
                });
            } catch (WizardStepValidationException ex) {
                ((InteractionUI) UI.getCurrent()).showNotification("Validation cash deposit", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        } else if (WizardStepType.Success.ordinal() == getStep()) {
            setStep(WizardStepType.Deposit.ordinal());
            getCurrentWizardStep().setupStep(new CreateBankCashDepositWizardStep.CreateBankCashDepositWizardStepParams(getAccountCashDeposit()));
        }
    }

    @Override
    protected void decorateStep() {

        layoutContent.removeAllComponents();
        layoutContent.addComponent(getSteps().get(WizardStepType.values()[getStep()].getViewIndex()));

        if (getStep() == WizardStepType.Deposit.ordinal()) {
            setUpWizardControl("Add general data", "", false, "Next", true);
        } else if (getStep() == WizardStepType.Nominal.ordinal()) {
            setUpWizardControl("Add nominals", "Back", true, "Next", true);
        } else if (getStep() == WizardStepType.Discrepancy.ordinal()) {
            setUpWizardControl("Add discrepancies", "Back", true, "Save", true);
        } else if (getStep() == WizardStepType.Success.ordinal()) {
            setUpWizardControl("Successfully created", "", false, "New", true);
        } else if (getStep() == WizardStepType.Fail.ordinal()) {
            setUpWizardControl("Failed created", "Back", true, "", false);
        }
    }
}
