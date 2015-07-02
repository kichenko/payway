/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.vaadin.ui.Panel;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public abstract class AbstractWizard extends Panel {

    private static final long serialVersionUID = 5373900918341617583L;

    protected int step;
    protected int stepCount;
    protected MessageServerSenderService service;
    protected List<AbstractWizardStep> steps = new ArrayList<>(0);

    public AbstractWizard(int stepCount) {
        setStepCount(stepCount);
        steps = new ArrayList<>(stepCount);
    }

    protected abstract void init();

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
