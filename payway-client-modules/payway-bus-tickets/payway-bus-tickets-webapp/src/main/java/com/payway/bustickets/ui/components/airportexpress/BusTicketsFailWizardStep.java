/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.airportexpress;

import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;

/**
 * BusTicketsParamsWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
public class BusTicketsFailWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = 8060109602379331780L;

    public BusTicketsFailWizardStep() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsFailWizardStep.xml", this));
    }
}
