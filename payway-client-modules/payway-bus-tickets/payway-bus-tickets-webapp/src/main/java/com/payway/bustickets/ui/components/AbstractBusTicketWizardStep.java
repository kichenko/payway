/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components;

import com.payway.commons.webapp.ui.components.wizard.AbstractStandartButtonWizard;
import lombok.NoArgsConstructor;

/**
 * AbstractBusTicketWizardStep
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@NoArgsConstructor
public abstract class AbstractBusTicketWizardStep extends AbstractStandartButtonWizard {

    private static final long serialVersionUID = -5037544000737605342L;

    public abstract void setLogoImage(byte[] content);
    
    public AbstractBusTicketWizardStep(int stepCount) {
        super(stepCount);
    }
}
