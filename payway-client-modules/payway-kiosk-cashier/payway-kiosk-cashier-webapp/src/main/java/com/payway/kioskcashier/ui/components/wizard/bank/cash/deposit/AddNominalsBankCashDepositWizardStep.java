/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BankCashDepositModel;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.BankNoteCashModel;
import com.payway.kioskcashier.ui.components.wizard.bank.cash.deposit.datasource.NoteCountingDepositModel;
import com.payway.kioskcashier.ui.core.BankNoteNominalsTableFieldFactory;
import com.payway.kioskcashier.ui.core.BanknoteNominalModelBeanContainer;
import com.payway.kioskcashier.ui.core.KeyboardNavigatorHandler;
import com.payway.kioskcashier.ui.model.core.BanknoteNominalModel;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.CashDepositCountingNominalsSummaryRequest;
import com.payway.messaging.message.kioskcashier.CashDepositCountingNominalsSummaryResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.BankCashDepositCountingSummaryDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AddNominalsBankCashDepositWizardStep
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Slf4j
public final class AddNominalsBankCashDepositWizardStep extends AbstractBankCashDepositBeanItemWizardStep {

    private static final long serialVersionUID = 4042120283975311464L;

    private static final Function<NoteCountingDepositModel, Long> transformerNoteCountingDepositModel = new Function<NoteCountingDepositModel, Long>() {

        @Override
        public Long apply(NoteCountingDepositModel src) {

            if (src != null) {
                return src.getId();
            }

            return 0L;
        }
    };

    @Getter
    @AllArgsConstructor
    public static final class AddNominalsBankCashDepositWizardStepParams extends AbstractWizardStepParams {

        private final CurrencyDto currency;
        private final List<BanknoteNominalDto> nominals;
    }

    @UiField
    private Table gridCashDepositNominals;

    private final Panel panelNavigator = new Panel();

    private final Map<Long, TextField> mapGridCashDepositNominalsEditors = new HashMap<>();

    private final BanknoteNominalModelBeanContainer gridCashDepositNominalsContainer = new BanknoteNominalModelBeanContainer();

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private CurrencyDto currency;

    public AddNominalsBankCashDepositWizardStep() {
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        panelNavigator.setSizeFull();
        panelNavigator.addActionHandler(new KeyboardNavigatorHandler(gridCashDepositNominalsContainer, mapGridCashDepositNominalsEditors));
        panelNavigator.setContent(Clara.create("AddNominalsBankCashDepositWizardStep.xml", this));
        addComponent(panelNavigator);

        gridCashDepositNominals.setImmediate(true);
        gridCashDepositNominals.setContainerDataSource(gridCashDepositNominalsContainer);

        //header
        gridCashDepositNominals.setColumnHeader("label", "Nominal");
        gridCashDepositNominals.setColumnHeader("quantity", "Quantity");
        gridCashDepositNominals.setColumnHeader("amount", "Amount");

        //footer
        gridCashDepositNominals.setFooterVisible(true);

        //column align
        gridCashDepositNominals.setColumnAlignment("label", Table.Align.CENTER);
        gridCashDepositNominals.setColumnAlignment("quantity", Table.Align.RIGHT);
        gridCashDepositNominals.setColumnAlignment("amount", Table.Align.RIGHT);

        gridCashDepositNominals.setVisibleColumns("label", "quantity", "amount");

        gridCashDepositNominals.setEditable(true);
    }

    private void refreshFooter() {

        int totalQuantity = 0;
        double totalAmount = 0.0;

        for (Long iid : gridCashDepositNominalsContainer.getItemIds()) {
            BanknoteNominalModel bean = (BanknoteNominalModel) gridCashDepositNominalsContainer.getItem(iid).getBean();
            if (bean != null) {
                totalQuantity += bean.getQuantity();
                totalAmount += bean.getQuantity() * bean.getNominal();
            }
        }

        gridCashDepositNominals.setColumnFooter("label", "Total:");
        gridCashDepositNominals.setColumnFooter("quantity", NumberFormatConverterUtils.format(totalQuantity, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS));
        gridCashDepositNominals.setColumnFooter("amount", String.format("%s %s", NumberUtils.isInteger(totalAmount) ? NumberFormatConverterUtils.format(totalAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(totalAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getCurrency().getIso()));
    }

    @Override
    public void setupStep(AbstractWizardStepParams state) {

        AddNominalsBankCashDepositWizardStepParams st = (AddNominalsBankCashDepositWizardStepParams) state;

        setCurrency(st.getCurrency());
        gridCashDepositNominals.setTableFieldFactory(new BankNoteNominalsTableFieldFactory(
                getCurrency().getIso(),
                mapGridCashDepositNominalsEditors,
                new BankNoteNominalsTableFieldFactory.FieldRefreshCallbackListener() {

                    @Override
                    public void refresh() {
                        refreshFooter();
                    }
                }));

        mapGridCashDepositNominalsEditors.clear();
        gridCashDepositNominalsContainer.removeAllItems();

        gridCashDepositNominalsContainer.addAll(Collections2.transform(st.getNominals(), new Function<BanknoteNominalDto, BanknoteNominalModel>() {
            @Override
            public BanknoteNominalModel apply(BanknoteNominalDto dto) {
                return new BanknoteNominalModel(dto.getId(), dto.getLabel(), dto.getNominal(), 0);
            }
        }));

        refreshFooter();
    }

    @Override
    public void next(final ActionWizardStepHandler listener, Object... args) {

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new CashDepositCountingNominalsSummaryRequest(Lists.newArrayList(Lists.transform(getBeanItem().getBean().getDepositCountings(), transformerNoteCountingDepositModel))),
                new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

                    @Override
                    public void doServerResponse(SuccessResponse response) {
                        if (response instanceof CashDepositCountingNominalsSummaryResponse) {
                            CashDepositCountingNominalsSummaryResponse rsp = (CashDepositCountingNominalsSummaryResponse) response;
                            calculate(rsp.getNominals(), rsp.getCountingSummary());
                            listener.success();
                        } else {
                            log.error("Bad server response (unknown type) - {}", response);
                            ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                            listener.exception(new Exception("Internal server error"));
                        }

                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                    }

                    @Override
                    public void doServerException(ExceptionResponse exception) {
                        log.error("Bad exception response (server exception) - {}", exception);
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                        listener.exception(new Exception("Internal server error"));
                    }

                    @Override
                    public void doLocalException(Exception exception) {
                        log.error("Bad exception response (local exception) - {}", exception);
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                        listener.exception(exception);
                    }

                    @Override
                    public void doTimeout() {
                        log.error("Bad exception response (time out)");
                        ((InteractionUI) UI.getCurrent()).closeProgressBar();
                        ((InteractionUI) UI.getCurrent()).showNotification("Server error", "Internal server error", Notification.Type.ERROR_MESSAGE);
                        listener.exception(new Exception("Internal server error"));
                    }
                }));
    }

    @Override
    public void previous(ActionWizardStepHandler listener, Object... args) {
        //
    }

    private void calculate(List<BanknoteNominalDto> nominals, List<BankCashDepositCountingSummaryDto> countingSummary) {

        double total = 0, shortageAmount = 0, surplusAmount = 0;
        BankCashDepositModel deposit = getBeanItem().getBean();

        Map<Long, BankCashDepositCountingSummaryDto> mapCountingNominals = Maps.uniqueIndex(countingSummary, new Function<BankCashDepositCountingSummaryDto, Long>() {
            @Override
            public Long apply(BankCashDepositCountingSummaryDto src) {
                return src.getNominalId();
            }
        });

        Map<Long, BanknoteNominalModel> mapDepositNominals = Maps.uniqueIndex(
                Lists.transform(gridCashDepositNominalsContainer.getItemIds(), new Function<Long, BanknoteNominalModel>() {
                    @Override
                    public BanknoteNominalModel apply(Long id) {
                        return gridCashDepositNominalsContainer.getItem(id).getBean();
                    }
                }),
                new Function<BanknoteNominalModel, Long>() {
                    @Override
                    public Long apply(BanknoteNominalModel src) {
                        return src.getId();
                    }
                });

        Map<Long, BanknoteNominalDto> mapNominals = Maps.uniqueIndex(nominals, new Function<BanknoteNominalDto, Long>() {
            @Override
            public Long apply(BanknoteNominalDto src) {
                return src.getId();
            }
        });

        deposit.setTotal(0);
        deposit.setSurplus(false);
        deposit.setShortage(false);
        getBeanItem().getBean().getDepositNominals().clear();

        for (Long nominalId : mapNominals.keySet()) {

            int delta = 0;
            int countingQuantity = 0;
            int cashQuantity = 0;

            BankNoteCashModel detail;
            BanknoteNominalDto nominal = mapNominals.get(nominalId);
            BanknoteNominalModel cashDto = mapDepositNominals.get(nominalId);
            BankCashDepositCountingSummaryDto countingDto = mapCountingNominals.get(nominalId);

            if (countingDto != null) {
                countingQuantity = countingDto.getQuantity();
            }

            if (cashDto != null) {
                cashQuantity = cashDto.getQuantity();
            }

            delta = cashQuantity - countingQuantity;

            //skip empty details
            if (cashQuantity == 0 && delta == 0) {
                continue;
            }

            detail = new BankNoteCashModel();
            detail.setDelta(delta);
            detail.setNominalId(nominalId);
            detail.setQuantity(cashQuantity);
            detail.setLabel(nominal.getLabel());
            detail.setNominal(nominal.getNominal());

            if (delta > 0) {
                deposit.setSurplus(true);
                surplusAmount += detail.getDelta() * detail.getNominal();
            } else if (delta < 0) {
                deposit.setShortage(true);
                shortageAmount += detail.getDelta() * detail.getNominal();
            }

            total += detail.getQuantity() * detail.getNominal();

            deposit.getDepositNominals().add(detail);
        }

        deposit.setTotal(total);
        deposit.setSurplusAmount(surplusAmount);
        deposit.setShortageAmount(Math.abs(shortageAmount));
    }

    @Override
    public void validate() throws WizardStepValidationException {

        int quantity = 0;
        for (Long iid : gridCashDepositNominalsContainer.getItemIds()) {
            BanknoteNominalModel bean = (BanknoteNominalModel) gridCashDepositNominalsContainer.getItem(iid).getBean();
            quantity += bean.getQuantity();
        }

        if (quantity == 0) {
            throw new WizardStepValidationException("Denominations not entered");
        }
    }
}
