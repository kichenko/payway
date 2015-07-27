/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

import com.payway.commons.webapp.messaging.MessageServerSenderService;
import com.payway.messaging.model.common.CurrencyDto;
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
    protected String sessionId;
    protected CurrencyDto currency;
    protected MessageServerSenderService service;
    protected List<AbstractWizardStep> steps = new ArrayList<>();

    protected abstract void init();

    protected abstract int getCurrentViewIndex();

    public AbstractWizard(int stepCount) {
        this.stepCount = stepCount;
    }

    public boolean setStep(int step) {
        if (step >= 0 && step < stepCount) {
            this.step = step;
            return true;
        }

        return false;
    }

    public AbstractWizardStep getWizardStep(int viewIndex) {
        return viewIndex >= 0 && viewIndex < steps.size() ? steps.get(viewIndex) : null;
    }

    protected AbstractWizardStep getCurrentWizardStep() {
        return getWizardStep(getCurrentViewIndex());
    }

    public void activate() {
        //
    }

    public void refresh() {
        //
    }
}
