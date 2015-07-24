/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.terminal.encashment;

import com.google.common.collect.Lists;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.kioskcashier.ui.components.wizard.common.AbstractCountingDiscrepancyWizardStep;
import com.payway.kioskcashier.ui.model.core.NoteCountingDiscrepancyModel;
import com.payway.kioskcashier.ui.model.core.NoteCountingDiscrepancyModelBeanItemContainer;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.EncashmentCountingDiscrepancySaveRequest;
import com.payway.messaging.message.kioskcashier.EncashmentCountingDiscrepancySaveResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.CountingDiscrepancyDto;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * CountingDiscrepancyWizardStep
 *
 * @author Sergey Kichenko
 * @created 13.07.15 00:00
 */
@Slf4j
public final class CountingDiscrepancyWizardStep extends AbstractCountingDiscrepancyWizardStep {

    private static final long serialVersionUID = -4034008235986621684L;

    @Getter
    @AllArgsConstructor
    public final static class CountingDiscrepancyWizardStepState extends AbstractWizardStep.AbstractWizardStepState {

        private final long countingId;
        private final List<CountingDiscrepancyDto> discrepancies;
    }

    @Getter
    @AllArgsConstructor
    public final static class CountingDiscrepancyWizardStepParams extends AbstractWizardStep.AbstractWizardStepParams {

        private final long countingId;
        private final CurrencyDto currency;
        private final double surplusAmount;
        private final double shortageAmount;
    }

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private CountingDiscrepancyWizardStepParams params;

    public CountingDiscrepancyWizardStep() {
        init();
    }

    @Override
    public void setupStep(AbstractWizardStepParams params) {
        setParams((CountingDiscrepancyWizardStep.CountingDiscrepancyWizardStepParams) params);
        super.setupStep(params);
    }

    @Override
    public CountingDiscrepancyWizardStepState getStepState() {

        NoteCountingDiscrepancyModelBeanItemContainer panelShortageContainer = (NoteCountingDiscrepancyModelBeanItemContainer) panelShortage.getGrid().getContainerDataSource();
        NoteCountingDiscrepancyModelBeanItemContainer panelSurplusContainer = (NoteCountingDiscrepancyModelBeanItemContainer) panelSurplus.getGrid().getContainerDataSource();

        List<NoteCountingDiscrepancyModel> discrepancies = new ArrayList<>(panelShortageContainer.getItemIds().size() + panelSurplusContainer.getItemIds().size());
        discrepancies.addAll(panelShortageContainer.getItemIds());
        discrepancies.addAll(panelSurplusContainer.getItemIds());

        return new CountingDiscrepancyWizardStepState(getParams().getCountingId(), Lists.newArrayList(Lists.transform(discrepancies, new TransformerNoteCountingDiscrepancyModel2Dto())));
    }

    @Override
    protected CurrencyDto getCurrency() {
        return params.getCurrency();
    }

    @Override
    protected double getSurplusAmount() {
        return params.getSurplusAmount();
    }

    @Override
    protected double getShortageAmount() {
        return params.getShortageAmount();
    }

    @Override
    public void next(final ActionWizardStepHandler listener, Object... args) {

        CountingDiscrepancyWizardStep.CountingDiscrepancyWizardStepState state = getStepState();

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new EncashmentCountingDiscrepancySaveRequest(state.getCountingId(), state.getDiscrepancies()),
                new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

                    @Override
                    public void doServerResponse(SuccessResponse response) {
                        if (response instanceof EncashmentCountingDiscrepancySaveResponse) {
                            listener.success(new Object[]{response});
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
                })
        );
    }
}
