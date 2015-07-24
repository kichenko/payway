/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.terminal.encashment;

import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;
import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStandartButtonStep;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.kioskcashier.ui.core.BankNoteNominalsTableFieldFactory;
import com.payway.kioskcashier.ui.core.BanknoteNominalModelBeanContainer;
import com.payway.kioskcashier.ui.core.KeyboardNavigatorHandler;
import com.payway.kioskcashier.ui.model.core.BanknoteNominalModel;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveFailureResponse;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveRequest;
import com.payway.messaging.message.kioskcashier.EncashmentCountingSaveResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalEncashmentDto;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
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
 * CrudWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Slf4j
public final class CrudWizardStep extends AbstractWizardStandartButtonStep {

    @Getter
    @AllArgsConstructor
    public static final class CrudWizardStepWizardStepParams extends AbstractWizardStepParams {

        private final KioskEncashmentDto kioskEncashment;
        private final List<BanknoteNominalDto> nominals;
        private final CurrencyDto currency;
    }

    private static final long serialVersionUID = -8297534233174351589L;

    @UiField
    private TextField editTerminal;

    @UiField
    private TextField editReport;

    @UiField
    private PopupDateField cbDateOccured;

    @UiField
    private Table gridEncashment;

    private final Panel panelNavigator = new Panel();

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private KioskEncashmentDto kioskEncashment;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private CurrencyDto currency;

    private final Map<Long, TextField> mapGridEncashmentContainerEditors = new HashMap<>();
    private final BanknoteNominalModelBeanContainer gridEncashmentContainer = new BanknoteNominalModelBeanContainer();

    public CrudWizardStep() {
        init();
    }

    @Override
    protected void init() {

        setSizeFull();

        panelNavigator.setSizeFull();
        panelNavigator.addActionHandler(new KeyboardNavigatorHandler(gridEncashmentContainer, mapGridEncashmentContainerEditors));
        panelNavigator.setContent(Clara.create("CrudWizardStep.xml", this));
        addComponent(panelNavigator);

        gridEncashment.setImmediate(true);
        gridEncashment.setContainerDataSource(gridEncashmentContainer);

        //header
        gridEncashment.setColumnHeader("label", "Nominal");
        gridEncashment.setColumnHeader("quantity", "Quantity");
        gridEncashment.setColumnHeader("amount", "Amount");

        //footer
        gridEncashment.setFooterVisible(true);

        //column align
        gridEncashment.setColumnAlignment("label", Table.Align.CENTER);
        gridEncashment.setColumnAlignment("quantity", Table.Align.RIGHT);
        gridEncashment.setColumnAlignment("amount", Table.Align.RIGHT);

        gridEncashment.setVisibleColumns("label", "quantity", "amount");

        gridEncashment.setEditable(true);

    }

    private void setReadOnlyFlag(boolean flag) {
        editTerminal.setReadOnly(flag);
        editReport.setReadOnly(flag);
        cbDateOccured.setReadOnly(flag);
    }

    private void refreshFooter() {

        int totalQuantity = 0;
        double totalAmount = 0.0;

        for (Long iid : gridEncashmentContainer.getItemIds()) {
            BanknoteNominalModel bean = (BanknoteNominalModel) gridEncashmentContainer.getItem(iid).getBean();
            if (bean != null) {
                totalQuantity += bean.getQuantity();
                totalAmount += bean.getQuantity() * bean.getNominal();
            }
        }

        gridEncashment.setColumnFooter("label", "Total:");
        gridEncashment.setColumnFooter("quantity", NumberFormatConverterUtils.format(totalQuantity, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS));
        gridEncashment.setColumnFooter("amount", String.format("%s %s", NumberUtils.isInteger(totalAmount) ? NumberFormatConverterUtils.format(totalAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(totalAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getCurrency().getIso()));
    }

    @Override
    public void setupStep(AbstractWizardStepParams state) {

        CrudWizardStepWizardStepParams st = (CrudWizardStepWizardStepParams) state;

        setReadOnlyFlag(false);

        setCurrency(st.getCurrency());
        setKioskEncashment(st.getKioskEncashment());

        gridEncashment.setTableFieldFactory(new BankNoteNominalsTableFieldFactory(
                getCurrency().getIso(),
                mapGridEncashmentContainerEditors,
                new BankNoteNominalsTableFieldFactory.FieldRefreshCallbackListener() {

                    @Override
                    public void refresh() {
                        refreshFooter();
                    }
                }));

        editTerminal.setValue(st.getKioskEncashment().getTerminalName());
        editReport.setValue(Integer.toString(st.getKioskEncashment().getSeqNum()));
        cbDateOccured.setValue(st.getKioskEncashment().getOccuredDate());

        mapGridEncashmentContainerEditors.clear();
        gridEncashmentContainer.removeAllItems();

        gridEncashmentContainer.addAll(Collections2.transform(st.getNominals(), new Function<BanknoteNominalDto, BanknoteNominalModel>() {
            @Override
            public BanknoteNominalModel apply(BanknoteNominalDto dto) {
                return new BanknoteNominalModel(dto.getId(), dto.getLabel(), dto.getNominal(), 0);
            }
        }));

        refreshFooter();
        setReadOnlyFlag(true);
    }

    public List<BanknoteNominalEncashmentDto> getEncashments() {

        List<BanknoteNominalEncashmentDto> encashments = new ArrayList<>(gridEncashmentContainer.getItemIds().size());
        for (Long iid : gridEncashmentContainer.getItemIds()) {
            BanknoteNominalModel bean = (BanknoteNominalModel) gridEncashmentContainer.getItem(iid).getBean();
            encashments.add(new BanknoteNominalEncashmentDto(bean.getId(), bean.getQuantity()));
        }

        return encashments;
    }

    @Override
    public void validate() throws WizardStepValidationException {

        int quantity = 0;
        for (Long iid : gridEncashmentContainer.getItemIds()) {
            BanknoteNominalModel bean = (BanknoteNominalModel) gridEncashmentContainer.getItem(iid).getBean();
            quantity += bean.getQuantity();
        }

        if (quantity == 0) {
            throw new WizardStepValidationException("Denominations not entered");
        }
    }

    @Override
    public void next(final ActionWizardStepHandler listener, Object... args) {

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new EncashmentCountingSaveRequest((String) args[0], getKioskEncashment().getId(), getEncashments()), new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(SuccessResponse response) {

                if (response instanceof EncashmentCountingSaveResponse) {
                    listener.success(new Object[]{response});
                } else if (response instanceof EncashmentCountingSaveFailureResponse) {
                    listener.failure(new Object[]{response});
                } else {
                    log.error("Bad server response (unknown type) - {}", response);
                    listener.exception(new Exception("Bad server response (unknown type)"));
                }

                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                listener.exception(new Exception(exception.getMessage()));
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                listener.exception(exception);
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                listener.exception(new Exception("Time out"));
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }
        }));
    }

    @Override
    public void previous(ActionWizardStepHandler listener, Object... args) {
        //
    }
}
