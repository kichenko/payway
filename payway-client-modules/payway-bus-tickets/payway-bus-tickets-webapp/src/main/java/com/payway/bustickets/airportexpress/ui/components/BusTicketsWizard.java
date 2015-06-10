/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BusTicketsWizard
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
public class BusTicketsWizard extends AbstractWizard {

    private static final long serialVersionUID = 7922687104437893528L;

    private static final int STEP_COUNT = 4;

    @UiField
    private Button btnLeft;
    @UiField
    private Button btnRight;

    @UiField
    private VerticalLayout layoutContent;

    @UiField
    private Image imgLogo;

    public BusTicketsWizard() {
        super(STEP_COUNT);
        init();
    }

    private void init() {
        setSizeFull();
        setIcon(new ThemeResource("images/sidebar_airport_express_bus_tickets.png"));
        setContent(Clara.create("BusTicketsWizard.xml", this));

        imgLogo.setSource(new ThemeResource("images/airport_express_logo.gif"));

        getSteps().add(new BusTicketsParamsWizardStep());
        getSteps().add(new BusTicketsConfirmWizardStep());
        getSteps().add(new BusTicketsSuccessWizardStep());
        getSteps().add(new BusTicketsFailWizardStep());

        setStep(BusTicketsParamsWizardStep.STEP_NO);

        //
        btnLeft.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handleStepLeft();
                decorateStep();
            }
        });

        //
        btnRight.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handleStepRight();
                decorateStep();
            }
        });
    }

    private void decorateStep() {
        if (getStep() == BusTicketsParamsWizardStep.STEP_NO) { //begin            
            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 1 - Selection of parameters");

            btnLeft.setVisible(false);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Next");
        } else if (getStep() == BusTicketsConfirmWizardStep.STEP_NO) { //checkout  

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 2 - Confirmation and order");

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Checkout");

        } else if (getStep() == BusTicketsSuccessWizardStep.STEP_NO) { //success

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 3 - Successful order");

            btnLeft.setVisible(true);
            btnLeft.setCaption("New Buy");

            btnRight.setVisible(true);
            btnRight.setCaption("Save ticket");

        } else if (getStep() == BusTicketsFailWizardStep.STEP_NO) { //fail

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 3 - Failed order");

            btnLeft.setVisible(false);
            btnLeft.setCaption("");

            btnRight.setVisible(true);
            btnRight.setCaption("Try again");
        }
    }

    private void handleStepLeft() {
        if (BusTicketsConfirmWizardStep.STEP_NO == getStep()) {
            setStep(BusTicketsParamsWizardStep.STEP_NO);
        } else if (BusTicketsSuccessWizardStep.STEP_NO == getStep()) {
            setStep(BusTicketsParamsWizardStep.STEP_NO);
        }
    }

    private void handleStepRight() {
        if (BusTicketsParamsWizardStep.STEP_NO == getStep()) {
            processParams2ConfirmStep();
        } else if (BusTicketsConfirmWizardStep.STEP_NO == getStep()) {
            setStep(BusTicketsSuccessWizardStep.STEP_NO);
        } else if (BusTicketsFailWizardStep.STEP_NO == getStep()) {
            setStep(BusTicketsParamsWizardStep.STEP_NO);
        }
    }

    private void processParams2ConfirmStep() {
        BusTicketsParamsWizardStep wizardStep = (BusTicketsParamsWizardStep) getWizardStep(getStep());
        if (wizardStep != null) {
            if (wizardStep.validate()) {
                setStep(BusTicketsConfirmWizardStep.STEP_NO);
            }
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
}
