/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.components.terminal.encashment;

import com.payway.commons.webapp.ui.AbstractUI;
import com.payway.commons.webapp.ui.components.wizard.AbstractStandartButtonWizard;
import com.vaadin.server.ThemeResource;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBoxListener;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;

/**
 * TerminalEncashmentWizard
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@Slf4j
public final class TerminalEncashmentWizard extends AbstractStandartButtonWizard {

    private static final long serialVersionUID = -4237687965034396262L;

    public static final int STEP_COUNT = 3;

    public static final int TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID = 0;
    public static final int TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID = 1;
    public static final int TERMINAL_ENCASHMENT_FAIL_WIZARD_STEP_ID = 2;

    @Setter(AccessLevel.PRIVATE)
    boolean isHandleRightClick = false;

    public TerminalEncashmentWizard() {
        super(STEP_COUNT);
        init();
    }

    @Override
    protected void init() {

        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_terminal_encashment.png"));
        setContent(Clara.create("TerminalEncashmentWizard.xml", this));

        setUpSteps();
        setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
    }

    private void setUpSteps() {
        getSteps().add(new TerminalEncashmentCrudWizardStep());
        getSteps().add(new TerminalEncashmentSuccessWizardStep());
        getSteps().add(new TerminalEncashmentFailWizardStep());
    }

    @Override
    protected void handleStepLeft() {
        if (TERMINAL_ENCASHMENT_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        }
    }

    @Override
    protected void handleStepRight() {

        if (TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID == getStep()) {
            ((AbstractUI) getUI()).showMessageBox("Save terminal encashment report", "Are you sure want to save terminal encashment report", Icon.QUESTION, new MessageBoxListener() {
                @Override
                public void buttonClicked(ButtonId buttonId) {
                    if (ButtonId.YES.equals(buttonId)) {
                        processCrudStep();
                        setHandleRightClick(true);
                    } else {
                        setHandleRightClick(false);
                    }
                }
            },
                    ButtonId.YES,
                    ButtonId.NO
            );

        } else if (TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID == getStep() || TERMINAL_ENCASHMENT_FAIL_WIZARD_STEP_ID == getStep()) {
            setStep(TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID);
        }
    }

    @Override
    protected boolean isHandleRightClick() {
        return isHandleRightClick;
    }

    @Override
    protected void decorateStep() {

        if (getStep() == TERMINAL_ENCASHMENT_CRUD_WIZARD_STEP_ID) { //begin            
            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Create");

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("Save");

        } else if (getStep() == TERMINAL_ENCASHMENT_FAIL_WIZARD_STEP_ID) { //fail  

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Failed");

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("New");

        } else if (getStep() == TERMINAL_ENCASHMENT_SUCCESS_WIZARD_STEP_ID) { //success

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Successful");

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("New");
        }
    }

    @Override
    public boolean setStep(int step) {

        if (super.setStep(step)) {
            decorateStep();
            return true;
        }

        return false;
    }

    private void processCrudStep() {
        //
    }
}
