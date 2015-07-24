/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

import lombok.extern.slf4j.Slf4j;

/**
 * AbstractWizardStandartButtonStep
 *
 * @author Sergey Kichenko
 * @created 20.07.15 00:00
 */
@Slf4j
public abstract class AbstractWizardStandartButtonStep extends AbstractWizardStep {

    private static final long serialVersionUID = -1204613898620095944L;

    public interface ActionWizardStepHandler {

        void success(Object... args);

        void failure(Object... args);

        void exception(Exception ex);
    }

    public abstract void next(final ActionWizardStepHandler listener, final Object... args);

    public abstract void previous(final ActionWizardStepHandler listener, final Object... args);

}
