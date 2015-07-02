/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import org.vaadin.teemu.clara.Clara;

/**
 * TerminalEncashmentFailWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
public final class TerminalEncashmentFailWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -46604939612244693L;

    public TerminalEncashmentFailWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("TerminalEncashmentFailWizardStep.xml", this));
    }
}
