/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import org.vaadin.teemu.clara.Clara;

/**
 * SuccessWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
public final class SuccessWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -4034008235986621684L;

    public SuccessWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("SuccessWizardStep.xml", this));
    }
}
