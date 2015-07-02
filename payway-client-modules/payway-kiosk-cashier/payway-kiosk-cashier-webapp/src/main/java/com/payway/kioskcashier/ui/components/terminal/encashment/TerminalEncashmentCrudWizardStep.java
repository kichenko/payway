/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import org.vaadin.teemu.clara.Clara;

/**
 * TerminalEncashmentCrudWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
public final class TerminalEncashmentCrudWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -8297534233174351589L;

    public TerminalEncashmentCrudWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("TerminalEncashmentCrudWizardStep.xml", this));
    }
}
