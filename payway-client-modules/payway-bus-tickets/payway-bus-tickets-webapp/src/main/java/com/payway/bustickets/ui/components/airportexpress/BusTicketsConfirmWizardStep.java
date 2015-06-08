/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.airportexpress;

import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;

/**
 * BusTicketsConfirmWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
public class BusTicketsConfirmWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -4226906157266350856L;

    public BusTicketsConfirmWizardStep() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsConfirmWizardStep.xml", this));
    }
}
