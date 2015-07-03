/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AbstractStandartButtonWizard
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractStandartButtonWizard extends AbstractWizard {

    private static final long serialVersionUID = -8946191934623942053L;

    @UiField
    protected VerticalLayout layoutContent;

    @UiField
    protected Button btnLeft;

    @UiField
    protected Button btnRight;

    public AbstractStandartButtonWizard(int stepCount) {
        super(stepCount);
    }

    protected abstract void handleStepLeft();

    protected abstract void handleStepRight();

    protected abstract void decorateStep();

    protected boolean isHandleLeftClick() {
        return true;
    }

    protected boolean isHandleRightClick() {
        return true;
    }
}
