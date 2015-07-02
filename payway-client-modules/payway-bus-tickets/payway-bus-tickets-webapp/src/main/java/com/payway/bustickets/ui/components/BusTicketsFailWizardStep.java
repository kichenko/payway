/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components;

import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.vaadin.ui.Label;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BusTicketsParamsWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Getter
public final class BusTicketsFailWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = 8060109602379331780L;

    @UiField
    private Label lbReason;

    public BusTicketsFailWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsFailWizardStep.xml", this));
    }
}
