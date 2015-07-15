/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.kioskcashier.ui.components.common.PanelTableButtons;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.NoteCountingDiscrepancyModel;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.NoteCountingDiscrepancyModelBeanItemContainer;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.ClarificationTypeDto;
import com.payway.messaging.model.kioskcashier.CountingDiscrepancyDto;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * CountingDiscrepancyWizardStep
 *
 * @author Sergey Kichenko
 * @created 13.07.15 00:00
 */
public final class CountingDiscrepancyWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -4034008235986621684L;

    public static interface FooterSummaryCalculation {

        double calculate();
    }

    @AllArgsConstructor
    private static class TransformerNoteCountingDiscrepancyModel2Dto implements Function<NoteCountingDiscrepancyModel, CountingDiscrepancyDto> {

        private final ClarificationTypeDto clarificationType;

        @Override
        public CountingDiscrepancyDto apply(NoteCountingDiscrepancyModel src) {
            return new CountingDiscrepancyDto(clarificationType, src.getClarification(), src.getAmount());
        }
    };

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

    @UiField
    private Label lbWarning;

    @Getter
    @UiField
    private Label lbMessage;

    @UiField
    private PanelTableButtons panelSurplus;

    @UiField
    private PanelTableButtons panelShortage;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private CountingDiscrepancyWizardStepParams params;

    public CountingDiscrepancyWizardStep() {
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        addComponent(Clara.create("CountingDiscrepancyWizardStep.xml", this));

        lbWarning.setValue(FontAwesome.WARNING.getHtml() + " Warning");

        //panelSurplus
        setUpPanelTable(panelSurplus, new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                panelSurplus.getGrid().getContainerDataSource().addItem(new NoteCountingDiscrepancyModel());
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
                panelShortage.getGrid().getContainerDataSource().addItem(new NoteCountingDiscrepancyModel());
            }
        }, new FooterSummaryCalculation() {

            @Override
            public double calculate() {
                return calculateFooter(panelShortage.getGrid());
            }
        });
    }

    private double calculateFooter(Table grid) {

        double totalAmount = 0.0;
        NoteCountingDiscrepancyModelBeanItemContainer container = (NoteCountingDiscrepancyModelBeanItemContainer) grid.getContainerDataSource();
        for (NoteCountingDiscrepancyModel bean : container.getItemIds()) {
            totalAmount += bean.getAmount();
        }

        return totalAmount;
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

    private String formatAmountWithCurrency(double amount) {
        return String.format("%s %s", NumberUtils.isInteger(amount) ? NumberFormatConverterUtils.format(amount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(amount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getParams().getCurrency().getIso());
    }

    @Override
    public void setupStep(AbstractWizardStepParams params) {

        setParams((CountingDiscrepancyWizardStepParams) params);

        panelSurplus.getGrid().getContainerDataSource().removeAllItems();
        panelShortage.getGrid().getContainerDataSource().removeAllItems();

        panelSurplus.getGrid().setColumnFooter("amount", "");
        panelShortage.getGrid().setColumnFooter("amount", "");

        lbMessage.setValue("Counting discrepancy found");
        panelSurplus.setCaption(String.format("<b>Surplus is %s</b>", formatAmountWithCurrency(getParams().getSurplusAmount())));
        panelShortage.setCaption(String.format("<b>Shortage is %s</b>", formatAmountWithCurrency(Math.abs(getParams().getShortageAmount()))));
    }

    @Override
    public CountingDiscrepancyWizardStepState getStepState() {

        List<NoteCountingDiscrepancyModel> shortageItemIds = ((NoteCountingDiscrepancyModelBeanItemContainer) panelShortage.getGrid().getContainerDataSource()).getItemIds();
        List<NoteCountingDiscrepancyModel> surplusItemIds = ((NoteCountingDiscrepancyModelBeanItemContainer) panelSurplus.getGrid().getContainerDataSource()).getItemIds();
        List<CountingDiscrepancyDto> discrepancies = new ArrayList<>(shortageItemIds.size() + surplusItemIds.size());

        discrepancies.addAll(Lists.transform(shortageItemIds, new TransformerNoteCountingDiscrepancyModel2Dto(ClarificationTypeDto.Shortage)));
        discrepancies.addAll(Lists.transform(surplusItemIds, new TransformerNoteCountingDiscrepancyModel2Dto(ClarificationTypeDto.Surplus)));

        return new CountingDiscrepancyWizardStepState(getParams().getCountingId(), discrepancies);
    }

    private void setUpPanelTable(final PanelTableButtons panelTable, Button.ClickListener addClickListener, final FooterSummaryCalculation calculation) {

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

                TextField field = new TextField();
                field.setSizeFull();

                if ("clarification".equals(propertyId)) {
                    field.setNullRepresentation("");
                } else if ("amount".equals(propertyId)) {

                    field.setImmediate(true);
                    field.setStyleName("app-common-style-text-right");
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

                btn.setIcon(FontAwesome.MINUS);
                btn.addStyleName("tiny danger");
                btn.addClickListener(new Button.ClickListener() {
                    private static final long serialVersionUID = 5019806363620874205L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        source.getContainerDataSource().removeItem(itemId);
                        calculateFooter(source);
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
    public boolean validate() {
        return validateGridItems(panelSurplus.getGrid()) && validateGridItems(panelShortage.getGrid());
    }
}
