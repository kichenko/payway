/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BankCashDepositModel;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BankNoteCashModel;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.NoteCountingDepositModel;
import com.payway.kioskcashier.ui.components.wizard.common.AbstractCountingDiscrepancyWizardStep;
import com.payway.kioskcashier.ui.model.core.NoteCountingDiscrepancyModel;
import com.payway.kioskcashier.ui.model.core.NoteCountingDiscrepancyModelBeanItemContainer;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.CashDepositSaveRequest;
import com.payway.messaging.message.kioskcashier.CashDepositSaveResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.CashDepositDto;
import com.payway.messaging.model.kioskcashier.CashDepositNominalDto;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * DiscrepancyBankCashDepositWizardStep
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Slf4j
public final class DiscrepancyBankCashDepositWizardStep extends AbstractCountingDiscrepancyWizardStep implements BeanItemWizardStep<BankCashDepositModel> {

    private static final long serialVersionUID = 6744910676706757327L;

    public static Function<BankNoteCashModel, CashDepositNominalDto> transformerCashDepositNominal = new Function<BankNoteCashModel, CashDepositNominalDto>() {

        @Override
        public CashDepositNominalDto apply(BankNoteCashModel src) {

            if (src != null) {
                return new CashDepositNominalDto(src.getNominalId(), src.getQuantity(), src.getDelta());
            }
            return null;
        }
    };

    public static Function<NoteCountingDepositModel, Long> transformerNoteCountingDepositModel = new Function<NoteCountingDepositModel, Long>() {

        @Override
        public Long apply(NoteCountingDepositModel src) {

            if (src != null) {
                return src.getId();
            }
            return null;
        }
    };

    public static Function<BankCashDepositModel, CashDepositDto> transformerCashDeposit = new Function<BankCashDepositModel, CashDepositDto>() {

        @Override
        public CashDepositDto apply(BankCashDepositModel src) {

            return new CashDepositDto(
                    src.getCreated(),
                    src.getDepositedBy().getId(),
                    src.getAccount().getId(),
                    src.getTeller(),
                    src.isShortage(),
                    src.isSurplus(),
                    src.getTotal(),
                    Lists.newArrayList(Lists.transform(src.getDepositCountings(), transformerNoteCountingDepositModel)),
                    Lists.newArrayList(Lists.transform(src.getDepositNominals(), transformerCashDepositNominal)),
                    Lists.newArrayList(Lists.transform(src.getDiscrepancies(), new TransformerNoteCountingDiscrepancyModel2Dto()))
            );
        }
    };

    @Getter
    @AllArgsConstructor
    public final static class DiscrepancyBankCashDepositWizardStepParams extends AbstractWizardStep.AbstractWizardStepParams {

        private final CurrencyDto currency;
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private DiscrepancyBankCashDepositWizardStepParams params;

    private BeanItem<BankCashDepositModel> beanItem;

    public DiscrepancyBankCashDepositWizardStep() {
        init();
    }

    @Override
    protected CurrencyDto getCurrency() {
        return getParams().getCurrency();
    }

    @Override
    protected double getSurplusAmount() {

        double sum = 0.0;
        if (getBeanItem().getBean().isSurplus()) {
            for (BankNoteCashModel nominal : getBeanItem().getBean().getDepositNominals()) {
                if (nominal.getDelta() > 0) {
                    sum += nominal.getDelta() * nominal.getNominal();
                }
            }
        }

        return sum;
    }

    @Override
    protected double getShortageAmount() {

        double sum = 0.0;
        if (getBeanItem().getBean().isShortage()) {
            for (BankNoteCashModel nominal : getBeanItem().getBean().getDepositNominals()) {
                if (nominal.getDelta() < 0) {
                    sum += nominal.getDelta() * nominal.getNominal();
                }
            }
        }

        return Math.abs(sum);
    }

    @Override
    public void setupStep(AbstractWizardStepParams params) {
        setParams((DiscrepancyBankCashDepositWizardStepParams) params);
        super.setupStep(params);
    }

    @Override
    public void setBeanItem(BeanItem<BankCashDepositModel> value) {
        beanItem = value;
    }

    @Override
    public BeanItem<BankCashDepositModel> getBeanItem() {
        return beanItem;
    }

    @Override
    public Class getBeanItemClass() {
        return BankCashDepositModel.class;
    }

    @Override
    public void validate() throws WizardStepValidationException {
        super.validate();
    }

    @Override
    public void next(final ActionWizardStepHandler listener, Object... args) {

        NoteCountingDiscrepancyModelBeanItemContainer panelShortageContainer = (NoteCountingDiscrepancyModelBeanItemContainer) panelShortage.getGrid().getContainerDataSource();
        NoteCountingDiscrepancyModelBeanItemContainer panelSurplusContainer = (NoteCountingDiscrepancyModelBeanItemContainer) panelSurplus.getGrid().getContainerDataSource();

        List<NoteCountingDiscrepancyModel> discrepancies = new ArrayList<>(panelShortageContainer.getItemIds().size() + panelSurplusContainer.getItemIds().size());
        discrepancies.addAll(panelShortageContainer.getItemIds());
        discrepancies.addAll(panelSurplusContainer.getItemIds());

        getBeanItem().getBean().getDiscrepancies().clear();
        getBeanItem().getBean().getDiscrepancies().addAll(discrepancies);

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new CashDepositSaveRequest(transformerCashDeposit.apply(getBeanItem().getBean())), new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {

                if (response instanceof CashDepositSaveResponse) {
                    listener.success();
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    listener.exception(new Exception("Bad server response (unknown type)"));
                    ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                }

                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                listener.exception(new Exception(exception.getMessage()));
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                listener.exception(exception);
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                listener.exception(new Exception("Time out"));
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }
        }));
    }
}
