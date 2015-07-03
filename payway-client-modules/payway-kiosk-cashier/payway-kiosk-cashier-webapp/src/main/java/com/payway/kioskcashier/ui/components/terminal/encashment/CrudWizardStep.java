/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.messaging.message.kioskcashier.EncashmentReportSearchResponse;
import com.vaadin.ui.Grid;
import com.vaadin.ui.PopupDateField;
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
    private Grid gridEncashment;

    public CrudWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("CrudWizardStep.xml", this));
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
        setReadOnlyFlag(true);
    }

    @Override
    public boolean validate() {
        return false;
    }
}
