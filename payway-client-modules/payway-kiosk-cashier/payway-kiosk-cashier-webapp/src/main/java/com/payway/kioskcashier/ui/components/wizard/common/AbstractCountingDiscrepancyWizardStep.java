/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.wizard.common;

import com.google.common.base.Function;
import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStandartButtonStep;
import com.payway.commons.webapp.ui.components.wizard.WizardStepValidationException;
import com.payway.kioskcashier.ui.components.common.PanelTableButtons;
import com.payway.kioskcashier.ui.model.core.ClarificationTypeModel;
import com.payway.kioskcashier.ui.model.core.NoteCountingDiscrepancyModel;
import com.payway.kioskcashier.ui.model.core.NoteCountingDiscrepancyModelBeanItemContainer;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.ClarificationTypeDto;
import com.payway.messaging.model.kioskcashier.CountingDiscrepancyDto;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Locale;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AbstractCountingDiscrepancyWizardStep
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Slf4j
public abstract class AbstractCountingDiscrepancyWizardStep extends AbstractWizardStandartButtonStep {

    private static final long serialVersionUID = -4034008235986621684L;

    public static interface FooterSummaryCalculation {

        double calculate();
    }

    public static class TransformerNoteCountingDiscrepancyModel2Dto implements Function<NoteCountingDiscrepancyModel, CountingDiscrepancyDto> {

        @Override
        public CountingDiscrepancyDto apply(NoteCountingDiscrepancyModel src) {
            return new CountingDiscrepancyDto(ClarificationTypeModel.Shortage.equals(src.getKind()) ? ClarificationTypeDto.Shortage : ClarificationTypeDto.Surplus, src.getClarification(), src.getAmount());
        }
    };

    @UiField
    protected Label lbWarning;

    @Getter
    @UiField
    protected Label lbMessage;

    @UiField
    protected PanelTableButtons panelSurplus;

    @UiField
    protected PanelTableButtons panelShortage;

    protected abstract CurrencyDto getCurrency();

    protected abstract double getSurplusAmount();

    protected abstract double getShortageAmount();

    @Override
    protected void init() {

        setSizeFull();
        addComponent(Clara.create("/com/payway/kioskcashier/ui/components/wizard/common/CountingDiscrepancyWizardStep.xml", this));

        lbWarning.setValue(FontAwesome.WARNING.getHtml() + " Warning");

        //panelSurplus
        setUpPanelTable(panelSurplus, new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                panelSurplus.getGrid().getContainerDataSource().addItem(new NoteCountingDiscrepancyModel(ClarificationTypeModel.Surplus));
            }
        }, new FooterSummaryCalculation() {

            @Override
            public double calculate() {
                return calculateFooter(panelSurplus.getGrid());
            }
        });

        //panelShortage
        setUpPanelTable(panelShortage, new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                panelShortage.getGrid().getContainerDataSource().addItem(new NoteCountingDiscrepancyModel(ClarificationTypeModel.Shortage));
            }
        }, new FooterSummaryCalculation() {

            @Override
            public double calculate() {
                return calculateFooter(panelShortage.getGrid());
            }
        });
    }

    @Override
    public void setupStep(AbstractWizardStepParams params) {

        String template = "<div><div style=\"display:inline;\"><b>%s</b></div><div style=\"display:inline; float:right;\"><b>%s</b></div></div>";

        panelSurplus.getGrid().getContainerDataSource().removeAllItems();
        panelShortage.getGrid().getContainerDataSource().removeAllItems();

        panelSurplus.getGrid().setColumnFooter("amount", "");
        panelShortage.getGrid().setColumnFooter("amount", "");

        lbMessage.setValue("Counting discrepancy found");
        panelSurplus.setCaption(String.format(template, "Surplus", formatAmountWithCurrency(getSurplusAmount())));
        panelShortage.setCaption(String.format(template, "Shortage", formatAmountWithCurrency(Math.abs(getShortageAmount()))));
    }

    private double calculateFooter(Table grid) {

        double totalAmount = 0.0;
        NoteCountingDiscrepancyModelBeanItemContainer container = (NoteCountingDiscrepancyModelBeanItemContainer) grid.getContainerDataSource();
        for (NoteCountingDiscrepancyModel bean : container.getItemIds()) {
            totalAmount += bean.getAmount();
        }

        return totalAmount;
    }

    private String formatAmountWithCurrency(double amount) {
        return String.format("%s %s", NumberUtils.isInteger(amount) ? NumberFormatConverterUtils.format(amount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(amount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getCurrency().getIso());
    }

    protected void setUpPanelTable(final PanelTableButtons panelTable, Button.ClickListener addClickListener, final FooterSummaryCalculation calculation) {

        panelTable.getGrid().setEditable(true);

        panelTable.getGrid().setImmediate(true);
        panelTable.getGrid().setContainerDataSource(new NoteCountingDiscrepancyModelBeanItemContainer());

        //header
        panelTable.getGrid().setColumnHeader("amount", "Amount");
        panelTable.getGrid().setColumnHeader("clarification", "Clarification");

        //footer
        panelTable.getGrid().setFooterVisible(true);

        //column align
        panelTable.getGrid().setColumnAlignment("amount", Table.Align.RIGHT);
        panelTable.getGrid().setColumnAlignment("clarification", Table.Align.LEFT);

        panelTable.getGrid().setVisibleColumns("amount", "clarification");

        panelTable.getGrid().setTableFieldFactory(new TableFieldFactory() {
            private static final long serialVersionUID = 2678008976536735736L;

            @Override
            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {

                AbstractTextField field = null;

                if ("clarification".equals(propertyId)) {

                    field = new TextArea();

                    field.setSizeFull();
                    field.setHeight("70px");
                    field.setImmediate(true);
                    field.setNullRepresentation("");
                    field.addValidator(new StringLengthValidator("Empty clarification", 1, Integer.MIN_VALUE, false));

                } else if ("amount".equals(propertyId)) {

                    field = new TextField();

                    field.setSizeFull();
                    field.setImmediate(true);
                    field.setStyleName("app-common-style-text-right");
                    field.addValidator(new DoubleRangeValidator("Empty amount", 0.1, Double.MAX_VALUE));
                    field.setConverter(new Converter<String, Double>() {
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

                    field.addValueChangeListener(new Property.ValueChangeListener() {
                        private static final long serialVersionUID = -382717228031608542L;

                        @Override
                        public void valueChange(Property.ValueChangeEvent event) {
                            if (calculation != null) {
                                panelTable.getGrid().setColumnFooter("amount", formatAmountWithCurrency(calculation.calculate()));
                            } else {
                                panelTable.getGrid().setColumnFooter("amount", "");
                            }
                        }
                    });
                }

                return field;
            }
        });

        panelTable.getGrid().addGeneratedColumn("", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(final Table source, final Object itemId, Object columnId) {

                Button btn = new Button();

                btn.setCaptionAsHtml(true);
                btn.setCaption("<b>X</b>");
                btn.addStyleName("tiny danger");
                btn.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 5019806363620874205L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        source.getContainerDataSource().removeItem(itemId);
                        source.setColumnFooter("amount", formatAmountWithCurrency(calculateFooter(source)));
                    }
                });

                VerticalLayout layout = new VerticalLayout(btn);
                layout.setComponentAlignment(btn, Alignment.MIDDLE_CENTER);

                return layout;
            }
        });

        panelTable.getBtnAdd().addClickListener(addClickListener);
    }

    @Override
    public void previous(AbstractWizardStandartButtonStep.ActionWizardStepHandler listener, Object... args) {
        //
    }

    private boolean validateGridItems(Table grid) {

        NoteCountingDiscrepancyModelBeanItemContainer container = (NoteCountingDiscrepancyModelBeanItemContainer) grid.getContainerDataSource();
        for (NoteCountingDiscrepancyModel bean : container.getItemIds()) {
            if (StringUtils.isBlank(bean.getClarification()) || bean.getAmount() == 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void validate() throws WizardStepValidationException {
        if (!validateGridItems(panelSurplus.getGrid()) || !validateGridItems(panelShortage.getGrid())) {
            throw new WizardStepValidationException("Bad discrepancy validation");
        }
    }
}
