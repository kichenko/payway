/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;
import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.core.utils.NumberUtils;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.BanknoteNominalEncashment;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.BanknoteNominalEncashmentContainerBean;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchResponse;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalEncashmentDto;
import com.payway.messaging.model.kioskcashier.KioskEncashmentDto;
import com.payway.vaadin.addons.ui.textfield.digit.DigitTextField;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
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
public final class CrudWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -8297534233174351589L;

    @UiField
    private TextField editTerminal;

    @UiField
    private TextField editReport;

    @UiField
    private PopupDateField cbDateOccured;

    @UiField
    private Table gridEncashment;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private KioskEncashmentDto kioskEncashment;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    private CurrencyDto currency;

    public CrudWizardStep() {
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        addComponent(Clara.create("CrudWizardStep.xml", this));

        gridEncashment.setImmediate(true);
        gridEncashment.setContainerDataSource(new BanknoteNominalEncashmentContainerBean());

        //header
        gridEncashment.setColumnHeader("label", "Denomination");
        gridEncashment.setColumnHeader("quantity", "Quantity");
        gridEncashment.setColumnHeader("amount", "Amount");

        //footer
        gridEncashment.setFooterVisible(true);
        gridEncashment.setColumnFooter("label", "Total:");
        gridEncashment.setColumnFooter("quantity", "");
        gridEncashment.setColumnFooter("total", "");

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
                    field.setReadOnly(true);
                    field.setStyleName("borderless app-common-style-text-center");

                    return field;

                } else if ("amount".equals(propertyId)) {

                    TextField field = new TextField((String) propertyId);
                    field.setSizeFull();
                    field.setReadOnly(true);
                    field.setStyleName("borderless app-common-style-text-right");

                    return field;

                } else {

                    final DigitTextField field = new DigitTextField();
                    field.setSizeFull();
                    field.setMaxLength(4);
                    field.setData(itemId);
                    field.setImmediate(true);
                    field.setNullRepresentation("0");
                    field.setConverter(Integer.class);
                    field.setNullSettingAllowed(true);
                    field.setInvalidAllowed(false);
                    field.setInvalidCommitted(false);
                    field.setStyleName("app-common-style-text-right");

                    field.addValueChangeListener(new Property.ValueChangeListener() {
                        private static final long serialVersionUID = -382717228031608542L;

                        @Override
                        public void valueChange(Property.ValueChangeEvent event) {
                            if (event.getProperty().getValue() != null) {
                                BanknoteNominalEncashment bean = ((BanknoteNominalEncashmentContainerBean) gridEncashment.getContainerDataSource()).getItem(field.getData()).getBean();
                                if (bean != null) {
                                    double cellAmount = bean.getNominal() * bean.getQuantity();
                                    String strAmount = String.format("%s %s", NumberUtils.isInteger(cellAmount) ? NumberFormatConverterUtils.format(cellAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS) : NumberFormatConverterUtils.format(cellAmount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS), getCurrency().getIso());
                                    ((BanknoteNominalEncashmentContainerBean) gridEncashment.getContainerDataSource()).getItem(field.getData()).getItemProperty("amount").setValue(strAmount);
                                }
                                refreshGridEncashmentFooter();
                            }
                        }
                    });

                    return field;
                }
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
        BanknoteNominalEncashmentContainerBean container = (BanknoteNominalEncashmentContainerBean) gridEncashment.getContainerDataSource();

        for (Long iid : container.getItemIds()) {
            BanknoteNominalEncashment bean = container.getItem(iid).getBean();
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
        gridEncashment.getContainerDataSource().removeAllItems();

        ((BanknoteNominalEncashmentContainerBean) gridEncashment.getContainerDataSource()).addAll(
                Collections2.transform(response.getNominals(), new Function<BanknoteNominalDto, BanknoteNominalEncashment>() {
                    @Override
                    public BanknoteNominalEncashment apply(BanknoteNominalDto dto) {
                        return new BanknoteNominalEncashment(dto.getId(), dto.getBanknoteType(), dto.getLabel(), dto.getNominal(), 0, "");
                    }
                }));

        setCurrency(currency);
        setKioskEncashment(response.getKioskEncashment());
        refreshGridEncashmentFooter();
        setReadOnlyFlag(true);
    }

    public List<BanknoteNominalEncashmentDto> getEncashments() {

        BanknoteNominalEncashmentContainerBean container = (BanknoteNominalEncashmentContainerBean) gridEncashment.getContainerDataSource();
        List<BanknoteNominalEncashmentDto> encashments = new ArrayList<>(container.getItemIds().size());

        for (Long iid : container.getItemIds()) {

            BanknoteNominalEncashment bean = container.getItem(iid).getBean();
            if (bean != null) {
                encashments.add(new BanknoteNominalEncashmentDto(bean.getId(), bean.getQuantity()));
            } else {
                log.error("Empty bean in encashment table with iid={}", iid);
            }
        }

        return encashments;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
