/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.google.gwt.thirdparty.guava.common.base.Function;
import com.google.gwt.thirdparty.guava.common.collect.Collections2;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.BanknoteNominalEncashment;
import com.payway.kioskcashier.ui.components.terminal.encashment.container.BanknoteNominalEncashmentContainerBean;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchResponse;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * CrudWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
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

    public CrudWizardStep() {
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        addComponent(Clara.create("CrudWizardStep.xml", this));

        gridEncashment.setContainerDataSource(new BanknoteNominalEncashmentContainerBean());

        gridEncashment.setColumnHeader("banknoteType", "Banknote type");
        gridEncashment.setColumnHeader("label", "Label");
        gridEncashment.setColumnHeader("nominal", "Nominal");
        gridEncashment.setColumnHeader("quantity", "Quantity");
        gridEncashment.setColumnHeader("total", "Total");

        gridEncashment.setColumnFooter("banknoteType", "");
        gridEncashment.setColumnFooter("label", "");
        gridEncashment.setColumnFooter("nominal", "");
        gridEncashment.setColumnFooter("quantity", "");
        gridEncashment.setColumnFooter("total", "");
    }

    private void setReadOnlyFlag(boolean flag) {
        editTerminal.setReadOnly(flag);
        editReport.setReadOnly(flag);
        cbDateOccured.setReadOnly(flag);
    }

    public void setUp(EncashmentReportSearchResponse response) {

        setReadOnlyFlag(false);

        editTerminal.setValue(response.getKioskEncashment().getTerminalName());
        editReport.setValue(Long.toHexString(response.getKioskEncashment().getSeqNum()));
        cbDateOccured.setValue(response.getKioskEncashment().getOccuredDate());
        gridEncashment.getContainerDataSource().removeAllItems();

        ((BanknoteNominalEncashmentContainerBean) gridEncashment.getContainerDataSource()).addAll(
                Collections2.transform(response.getNominals(), new Function<BanknoteNominalDto, BanknoteNominalEncashment>() {
                    @Override
                    public BanknoteNominalEncashment apply(BanknoteNominalDto dto) {
                        return new BanknoteNominalEncashment(dto.getId(), dto.getBanknoteType(), dto.getLabel(), dto.getNominal(), 0);
                    }
                }));

        setReadOnlyFlag(true);
    }

    @Override
    public boolean validate() {
        return false;
    }
}
