/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.components.wizard;

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
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

    @NoArgsConstructor
    public static class StandartButtonWizardKeyboardHandler implements Action.Handler {

        private static final long serialVersionUID = 8820513042467846195L;

        private final Action key_shift_enter = new ShortcutAction("Shift+Enter", ShortcutAction.KeyCode.ENTER, new int[]{ShortcutAction.ModifierKey.SHIFT});

        @Override
        public Action[] getActions(Object target, Object sender) {
            return new Action[]{key_shift_enter};
        }

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if (action == key_shift_enter) {
                if (sender instanceof AbstractStandartButtonWizard) {
                    ((AbstractStandartButtonWizard) sender).handleStepRight();
                }
            }
        }
    }

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

    protected void setUpWizardControl(String title, String btnLeftCaption, boolean btnLeftVisisble, String btnRightCaption, boolean btnRightVisisble) {

        setCaption(title);

        btnLeft.setVisible(btnLeftVisisble);
        btnLeft.setCaption(btnLeftCaption);

        btnRight.setVisible(btnRightVisisble);
        btnRight.setCaption(btnRightCaption);
    }

    @Override
    public boolean setStep(int step) {

        if (super.setStep(step)) {
            decorateStep();
            return true;
        }

        return false;
    }
}
