/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.terminal.encashment;

import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStandartButtonStep;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.kioskcashier.ui.components.common.KioskEncashmentSelectWindow;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchFailureResponse;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchRequest;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchResponse;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * SearchWizardStep
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@Slf4j
public final class SearchWizardStep extends AbstractWizardStandartButtonStep {

    private static final long serialVersionUID = -8297534233174351589L;

    @Getter
    @AllArgsConstructor
    public static final class SearchWizardStepState extends AbstractWizardStepState {

        private final String terminalName;
        private final int reportNo;
    }

    @UiField
    private TextField editTerminal;

    @UiField
    private DigitTextField editReport;

    public SearchWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("SearchWizardStep.xml", this));

        editTerminal.setRequired(true);
        editTerminal.setNullRepresentation("");
        editTerminal.addValidator(new NullValidator("Empty terminal name", false));
        editTerminal.addValidator(new StringLengthValidator("The minimum length of the string 3 characters", 3, Integer.MAX_VALUE, false));

        editReport.setRequired(true);
        editReport.setConverter(Integer.class);
    }

    @Override
    public SearchWizardStepState getStepState() {
        return new SearchWizardStepState(StringUtils.trimWhitespace(editTerminal.getValue()), (Integer) editReport.getConvertedValue());
    }

    @Override
    public void validate() throws WizardStepValidationException {

        editTerminal.setValidationVisible(false);
        editTerminal.setValidationVisible(false);

        try {
            editReport.validate();
            editTerminal.validate();
        } catch (Validator.InvalidValueException ex) {
            editReport.setValidationVisible(true);
            editTerminal.setValidationVisible(true);

            throw new WizardStepValidationException("Bad input validation");
        }
    }

    @Override
    public void next(final ActionWizardStepHandler listener, Object... args) {

        final SearchWizardStep.SearchWizardStepState state = getStepState();

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new EncashmentReportSearchRequest((String) args[0], state.getTerminalName(), state.getReportNo()), new UIResponseCallBackSupport(getUI(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(final SuccessResponse response) {
                if (response instanceof EncashmentReportSearchResponse) {
                    new KioskEncashmentSelectWindow(String.format("Select encashment terminal (with report #%d)", state.getReportNo()), ((EncashmentReportSearchResponse) response).getKioskEncashments(), new KioskEncashmentSelectWindow.SelectorListener() {
                        @Override
                        public void select(KioskEncashmentDto item) {
                            listener.success(new Object[]{item, ((EncashmentReportSearchResponse) response).getNominals()});
                        }
                    }).show();
                } else if (response instanceof EncashmentReportSearchFailureResponse) {
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
    public void previous(final ActionWizardStepHandler listener, Object... args) {
        //
    }
}
