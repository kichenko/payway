/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
public abstract class AbstractWizardStep extends VerticalLayout {

    public static abstract class AbstractWizardStepParams {
        //
    }

    public static abstract class AbstractWizardStepState {
        //
    }

    private static final long serialVersionUID = -804397699790880938L;

    protected abstract void init();

    public void setupStep(AbstractWizardStepParams state) {
        //
    }

    public AbstractWizardStepState getStepState() {
        return null;
    }

    public void validate() throws WizardStepValidationException {
        //
    }
}
