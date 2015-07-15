/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;
import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.BanknoteNominalEncashmentModel;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.BanknoteNominalEncashmentModelBeanContainer;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalEncashmentDto;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public final class CrudWizardStep extends AbstractWizardStep {

    @NoArgsConstructor
    private static class KeyboardNavigatorHandler implements Action.Handler {

        private static final long serialVersionUID = -2496992285303828227L;

        //private final Action key_tab = new ShortcutAction("Tab", ShortcutAction.KeyCode.TAB, null);
        //private final Action key_tab_shift = new ShortcutAction("Shift+Tab", ShortcutAction.KeyCode.TAB, new int[]{ShortcutAction.ModifierKey.SHIFT});
        private final Action key_down = new ShortcutAction("Down", ShortcutAction.KeyCode.ARROW_DOWN, null);
        private final Action key_up = new ShortcutAction("Up", ShortcutAction.KeyCode.ARROW_UP, null);
        private final Action key_enter = new ShortcutAction("Enter", ShortcutAction.KeyCode.ENTER, null);
        private final Action key_shift_enter = new ShortcutAction("Shift+Enter", ShortcutAction.KeyCode.ENTER, new int[]{ShortcutAction.ModifierKey.SHIFT});

        private BanknoteNominalEncashmentModelBeanContainer container;
        private Map<Long, TextField> mapEditors;

        public KeyboardNavigatorHandler(BanknoteNominalEncashmentModelBeanContainer container, Map<Long, TextField> mapEditors) {
            this.container = container;
            this.mapEditors = mapEditors;
        }

        @Override
        public Action[] getActions(Object target, Object sender) {
            return new Action[]{/*key_tab, key_tab_shift,*/key_down, key_up, key_enter, key_shift_enter};
        }

        @Override
        public void handleAction(Action action, Object sender, Object target) {

            if (target instanceof TextField) {

                long itemId = (Long) ((TextField) target).getData();
                if (action == key_enter || /*action == key_tab ||*/ action == key_down) {

                    int idx = container.getItemIds().indexOf(itemId);
                    if (idx >= 0 && (idx + 1 < container.getItemIds().size())) {
                        itemId = container.getItemIds().get(idx + 1);
                        TextField txt = mapEditors.get(itemId);
                        if (txt != null) {
                            txt.selectAll();
                            txt.focus();
                        }
                    }

                } else if (/*action == key_tab_shift ||*/action == key_up || action == key_shift_enter) {

                    int idx = container.getItemIds().indexOf(itemId);
                    if (idx >= 0 && (idx - 1) >= 0) {
                        itemId = container.getItemIds().get(idx - 1);
                        TextField txt = mapEditors.get(itemId);
                        if (txt != null) {
                            txt.selectAll();
                            txt.focus();
                        }
                    }

                }
            }
        }
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

    private Panel panelNavigator = new Panel();

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private KioskEncashmentDto kioskEncashment;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private CurrencyDto currency;

    private final Map<Long, TextField> mapGridEncashmentContainerEditors = new HashMap<>();
    private final BanknoteNominalEncashmentModelBeanContainer gridEncashmentContainer = new BanknoteNominalEncashmentModelBeanContainer();

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
        gridEncashment.setTableFieldFactory(new TableFieldFactory() {
            private static final long serialVersionUID = 2678008976536735736L;

            @Override
            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {

                if ("label".equals(propertyId)) {

                    TextField field = new TextField((String) propertyId);
                    field.setSizeFull();
                    field.setData(itemId);
                    field.setReadOnly(true);
                    field.setStyleName("borderless app-common-style-text-center");

                    return field;

                } else if ("amount".equals(propertyId)) {

                    TextField field = new TextField((String) propertyId);
                    field.setSizeFull();
                    field.setData(itemId);
                    field.setReadOnly(true);
                    field.setStyleName("borderless app-common-style-text-right");

                    field.setConverter(new Converter<String, Double>() {
                        private static final long serialVersionUID = -810375086450402998L;

                        @Override
                        public Double convertToModel(String value, Class<? extends Double> targetType, Locale locale) throws Converter.ConversionException {
                            return null;
                        }

                        @Override
                        public String convertToPresentation(Double value, Class<? extends String> targetType, Locale locale) throws Converter.ConversionException {
                            return String.format("%s %s", NumberUtils.isInteger(value) ? NumberFormatConverterUtils.format(value, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(value, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getCurrency().getIso());
                        }

                        @Override
                        public Class<Double> getModelType() {
                            return Double.class;
                        }

                        @Override
                        public Class<String> getPresentationType() {
                            return String.class;
                        }
                    });

                    return field;

                } else if ("quantity".equals(propertyId)) {

                    final TextField field = new DigitTextField();
                    field.setSizeFull();
                    field.setMaxLength(4);
                    field.setData(itemId);
                    field.setImmediate(true);
                    field.setInvalidAllowed(false);
                    field.setNullRepresentation("0");
                    field.setStyleName("app-common-style-text-right");

                    mapGridEncashmentContainerEditors.put((Long) itemId, field);

                    field.setConverter(new Converter<String, Integer>() {
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

                    field.addValueChangeListener(new Property.ValueChangeListener() {
                        private static final long serialVersionUID = -382717228031608542L;

                        @Override
                        public void valueChange(Property.ValueChangeEvent event) {

                            Property property = gridEncashmentContainer.getContainerProperty(field.getData(), "amount");
                            if (property instanceof MethodProperty) {
                                ((MethodProperty) property).fireValueChange();
                            }

                            refreshGridEncashmentFooter();
                        }
                    });

                    return field;
                }

                return null;
            }
        });
    }

    private void setReadOnlyFlag(boolean flag) {
        editTerminal.setReadOnly(flag);
        editReport.setReadOnly(flag);
        cbDateOccured.setReadOnly(flag);
    }

    private void refreshGridEncashmentFooter() {

        int totalQuantity = 0;
        double totalAmount = 0.0;

        for (Long iid : gridEncashmentContainer.getItemIds()) {
            BanknoteNominalEncashmentModel bean = (BanknoteNominalEncashmentModel) gridEncashmentContainer.getItem(iid).getBean();
            if (bean != null) {
                totalQuantity += bean.getQuantity();
                totalAmount += bean.getQuantity() * bean.getNominal();
            }
        }

        gridEncashment.setColumnFooter("label", "Total:");
        gridEncashment.setColumnFooter("quantity", NumberFormatConverterUtils.format(totalQuantity, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS));
        gridEncashment.setColumnFooter("amount", String.format("%s %s", NumberUtils.isInteger(totalAmount) ? NumberFormatConverterUtils.format(totalAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(totalAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getCurrency().getIso()));
    }

    public void setUp(EncashmentReportSearchResponse response, CurrencyDto currency) {

        setReadOnlyFlag(false);

        editTerminal.setValue(response.getKioskEncashment().getTerminalName());
        editReport.setValue(Integer.toString(response.getKioskEncashment().getSeqNum()));
        cbDateOccured.setValue(response.getKioskEncashment().getOccuredDate());

        mapGridEncashmentContainerEditors.clear();
        gridEncashmentContainer.removeAllItems();

        gridEncashmentContainer.addAll(
                Collections2.transform(response.getNominals(), new Function<BanknoteNominalDto, BanknoteNominalEncashmentModel>() {
                    @Override
                    public BanknoteNominalEncashmentModel apply(BanknoteNominalDto dto) {
                        return new BanknoteNominalEncashmentModel(dto.getId(), dto.getLabel(), dto.getNominal(), 0);
                    }
                }));

        setCurrency(currency);
        setKioskEncashment(response.getKioskEncashment());
        refreshGridEncashmentFooter();
        setReadOnlyFlag(true);
    }

    public List<BanknoteNominalEncashmentDto> getEncashments() {

        List<BanknoteNominalEncashmentDto> encashments = new ArrayList<>(gridEncashmentContainer.getItemIds().size());

        for (Long iid : gridEncashmentContainer.getItemIds()) {

            BanknoteNominalEncashmentModel bean = (BanknoteNominalEncashmentModel) gridEncashmentContainer.getItem(iid).getBean();
            if (bean != null) {
                encashments.add(new BanknoteNominalEncashmentDto(bean.getId(), bean.getQuantity()));
            } else {
                log.error("Empty bean in encashment grid with iid={}", iid);
            }
        }

        return encashments;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
