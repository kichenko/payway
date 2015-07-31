/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.view.core.workspace;

import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.commons.webapp.messaging.UIResponseCallBackSupport;
import com.payway.commons.webapp.ui.InteractionUI;
import com.payway.commons.webapp.ui.components.buiders.WindowBuilder;
import com.payway.commons.webapp.ui.components.decorators.ContentDecorator;
import com.payway.commons.webapp.ui.components.decorators.ContentDecoratorButtonOk;
import com.payway.commons.webapp.ui.components.wizard.SuccessWizardStep;
import com.payway.commons.webapp.ui.components.wizard.WarningWizardStep;
import com.payway.kioskcashier.ui.components.common.KioskEncashmentSelectWindow;
import com.payway.kioskcashier.ui.view.core.AbstractKioskCashierWorkspaceView;
import com.payway.messaging.core.response.ExceptionResponse;
import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.message.kioskcashier.QuickEncashmentCheckRequest;
import com.payway.messaging.message.kioskcashier.QuickEncashmentCheckResponse;
import com.payway.messaging.message.kioskcashier.QuickEncashmentReportSearchFailureResponse;
import com.payway.messaging.message.kioskcashier.QuickEncashmentReportSearchRequest;
import com.payway.messaging.message.kioskcashier.QuickEncashmentReportSearchResponse;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * TerminalEncashmentWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = QuickEncashmentCheckWorkspaceView.QUICK_ENCASHMENT_WORKSPACE_VIEW_ID)
public class QuickEncashmentCheckWorkspaceView extends AbstractKioskCashierWorkspaceView {

    public static final String QUICK_ENCASHMENT_WORKSPACE_VIEW_ID = WORKSPACE_VIEW_ID_PREFIX + "QuickEncashmentCheck";

    private static final long serialVersionUID = -4544172267545848748L;

    @UiField
    private PopupDateField cbDateOccured;

    @UiField
    private TextField editTerminal;

    @UiField
    private TextField editReportNo;

    @UiField
    private TextField editAmount;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("QuickEncashmentCheckWorkspaceView.xml", this));

        cbDateOccured.setValue(new Date());

        editTerminal.setValidationVisible(false);
        editTerminal.addValidator(new StringLengthValidator("The minimum length of the string 3 characters", 3, Integer.MAX_VALUE, false));
        editTerminal.setConverter(String.class);

        editReportNo.setNullRepresentation("0");
        editReportNo.setValidationVisible(false);
        editReportNo.addValidator(new IntegerRangeValidator("Must be greater than zero", 1, Integer.MAX_VALUE));
        editReportNo.setConverter(new Converter<String, Integer>() {
            private static final long serialVersionUID = 4048654578990565270L;
            private final StringToIntegerConverter converter = new StringToIntegerConverter();

            @Override
            public Integer convertToModel(String value, Class<? extends Integer> targetType, Locale locale) throws Converter.ConversionException {

                Integer result = 0;
                try {
                    result = converter.convertToModel(value, targetType, locale);
                } catch (Exception ex) {
                    //NOP
                }

                return result == null ? 0 : result;
            }

            @Override
            public String convertToPresentation(Integer value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
                return converter.convertToPresentation(value, targetType, locale);
            }

            @Override
            public Class<Integer> getModelType() {
                return converter.getModelType();
            }

            @Override
            public Class<String> getPresentationType() {
                return converter.getPresentationType();
            }
        });

        editAmount.setNullRepresentation("0");
        editAmount.setValidationVisible(false);
        editAmount.addValidator(new DoubleRangeValidator("Must be greater than zero", 1.0, Double.MAX_VALUE));
        editAmount.setConverter(new Converter<String, Double>() {
            private static final long serialVersionUID = -874295801443223068L;
            private final StringToDoubleConverter converter = new StringToDoubleConverter();

            @Override
            public Double convertToModel(String value, Class<? extends Double> targetType, Locale locale) throws Converter.ConversionException {

                Double result = 0.0;
                try {
                    result = converter.convertToModel(value, targetType, locale);
                } catch (Exception ex) {
                    //NOP
                }

                return result == null ? 0 : result;
            }

            @Override
            public String convertToPresentation(Double value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
                return converter.convertToPresentation(value, targetType, locale);
            }

            @Override
            public Class<Double> getModelType() {
                return converter.getModelType();
            }

            @Override
            public Class<String> getPresentationType() {
                return converter.getPresentationType();
            }
        });

    }

    @Override
    public void activate() {
        //
    }

    @UiHandler(value = "btnCheck")
    public void buttonClickLeft(Button.ClickEvent event) {

        if (!validate()) {
            ((InteractionUI) UI.getCurrent()).showNotification("Search terminal", "Please, enter correct values", Notification.Type.ERROR_MESSAGE);
            return;
        }

        sendSearchTerminalRequest(userAppService.getUser().getSessionId(), editTerminal.getValue(), (Integer) editReportNo.getConvertedValue(), cbDateOccured.getValue(), (Double) editAmount.getConvertedValue());
    }

    private boolean validate() {

        boolean valid = true;

        try {
            editTerminal.validate();
            editReportNo.validate();
            editAmount.validate();
        } catch (Exception ex) {
            valid = false;
        }

        editTerminal.setValidationVisible(true);
        editReportNo.setValidationVisible(true);
        editAmount.setValidationVisible(true);

        return valid;
    }

    private void sendSearchTerminalRequest(final String sessionId, final String terminalName, final int reportNo, final Date date, final double amount) {

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new QuickEncashmentReportSearchRequest(sessionId, terminalName, reportNo, date), new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(final SuccessResponse response) {

                if (response instanceof QuickEncashmentReportSearchResponse) {

                    List<KioskEncashmentDto> encashments = ((QuickEncashmentReportSearchResponse) response).getKioskEncashments();
                    if (encashments.size() == 1) {
                        sendQuickEncashmentCheckRequest(encashments.get(0).getId(), amount);
                    } else {
                        new KioskEncashmentSelectWindow(String.format("Select encashment terminal (with report #%d)", reportNo), encashments, new KioskEncashmentSelectWindow.SelectorListener() {
                            @Override
                            public void select(KioskEncashmentDto item) {
                                sendQuickEncashmentCheckRequest(item.getId(), amount);
                            }
                        }).show();
                    }
                } else if (response instanceof QuickEncashmentReportSearchFailureResponse) {
                    log.error("Bad server response - {}", response);
                    ((InteractionUI) UI.getCurrent()).showNotification("Search terminal", ((QuickEncashmentReportSearchFailureResponse) response).getReason(), Notification.Type.ERROR_MESSAGE);
                } else {
                    log.error("Bad server response - {}", response);
                    ((InteractionUI) UI.getCurrent()).showNotification("Search terminal", "Internal server error", Notification.Type.ERROR_MESSAGE);
                }

                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Search terminal", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Search terminal", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Search terminal", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }
        }));
    }

    private void sendQuickEncashmentCheckRequest(final long encashmentId, final double amount) {

        ((InteractionUI) UI.getCurrent()).showProgressBar();
        getService().sendMessage(new QuickEncashmentCheckRequest(encashmentId), new UIResponseCallBackSupport(UI.getCurrent(), new UIResponseCallBackSupport.ResponseCallBackHandler() {

            @Override
            public void doServerResponse(final SuccessResponse response) {

                if (response instanceof QuickEncashmentCheckResponse) {
                    showResultDialog(amount, ((QuickEncashmentCheckResponse) response).getAmount());
                } else {
                    log.error("Bad server response - {}", response);
                    ((InteractionUI) UI.getCurrent()).showNotification("Encashment check", "Internal server error", Notification.Type.ERROR_MESSAGE);
                }

                ((InteractionUI) UI.getCurrent()).closeProgressBar();
            }

            @Override
            public void doServerException(ExceptionResponse exception) {
                log.error("Bad exception response (server exception) - {}", exception);
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Encashment check", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void doLocalException(Exception exception) {
                log.error("Bad exception response (local exception) - {}", exception);
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Encashment check", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }

            @Override
            public void doTimeout() {
                log.error("Bad exception response (time out)");
                ((InteractionUI) UI.getCurrent()).closeProgressBar();
                ((InteractionUI) UI.getCurrent()).showNotification("Encashment check", "Internal server error", Notification.Type.ERROR_MESSAGE);
            }
        }));
    }

    private void showResultDialog(double amountClient, double amountServer) {

        final com.vaadin.ui.Component content;

        double dif = amountClient - amountServer;
        double absDif = Math.abs(dif);

        String recipeDif = String.format("%s %s", NumberUtils.isInteger(absDif) ? NumberFormatConverterUtils.format(absDif, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(absDif, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getSettingsAppService().getCurrency().getIso());

        if (dif > 0) {
            content = new WarningWizardStep(String.format("Surplus found %s", recipeDif));
        } else if (dif < 0) {
            content = new WarningWizardStep(String.format("Shortage found %s", recipeDif));
        } else {
            content = new SuccessWizardStep("No surplus or shortage found");
        }

        new WindowBuilder()
                .withCaption("Result check")
                .withContent(new ContentDecoratorButtonOk(content, new ContentDecoratorButtonOk.ButtonClickEvent() {

                    @Override
                    public void click(ContentDecorator decorator, Button.ClickEvent event) {
                        ((Window) decorator.getParent()).close();
                    }
                }))
                .withModal()
                .withClosable()
                .withSizeUndefined()
                .buildAndShow();
    }
}
