/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.ui.Panel;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * AbstractWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Getter
@Setter
public abstract class AbstractWizard extends Panel {

    protected List<AbstractWizardStep> steps;
    protected int stepCount;
    protected int step;

    public AbstractWizard() {
        steps = new ArrayList<>(0);
    }

    public AbstractWizard(int stepCount) {
        setStepCount(stepCount);
        steps = new ArrayList<>(stepCount);
    }

    public boolean setStep(int step) {
        if (step >= 0 && step < stepCount) {
            this.step = step;
            return true;
        }

        return false;
    }

    public AbstractWizardStep getWizardStep(int step) {
        if (step >= 0 && step < stepCount) {
            return steps.get(step);
        }
        return null;
    }
}
