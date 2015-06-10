/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

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

    public static final int STEP_NO = 1;

    private static final long serialVersionUID = -4226906157266350856L;

    public BusTicketsConfirmWizardStep() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsConfirmWizardStep.xml", this));
    }
}
