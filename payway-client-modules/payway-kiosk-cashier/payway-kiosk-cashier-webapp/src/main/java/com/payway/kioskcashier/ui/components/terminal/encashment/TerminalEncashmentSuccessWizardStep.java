/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import org.vaadin.teemu.clara.Clara;

/**
 * TerminalEncashmentSuccessWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
public final class TerminalEncashmentSuccessWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -4034008235986621684L;

    public TerminalEncashmentSuccessWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("TerminalEncashmentSuccessWizardStep.xml", this));
    }
}
