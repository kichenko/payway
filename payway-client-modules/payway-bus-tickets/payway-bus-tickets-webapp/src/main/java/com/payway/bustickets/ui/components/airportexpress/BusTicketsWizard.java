/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.airportexpress;

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

        setStep(0);
        getSteps().add(new BusTicketsParamsWizardStep());
        getSteps().add(new BusTicketsConfirmWizardStep());
        getSteps().add(new BusTicketsSuccessWizardStep());
        getSteps().add(new BusTicketsFailWizardStep());

        decorateStep();

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
        if (getStep() == 0) { //begin            
            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 1 - Selection of parameters");

            btnLeft.setVisible(false);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Next");
        } else if (getStep() == 1) { //checkout  

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 2 - Confirmation and order");

            btnLeft.setVisible(true);
            btnLeft.setCaption("Back");

            btnRight.setVisible(true);
            btnRight.setCaption("Checkout");

        } else if (getStep() == 2) { //success

            layoutContent.removeAllComponents();
            layoutContent.addComponent(getSteps().get(getStep()));

            setCaption("Step 3 - Successful order");

            btnLeft.setVisible(true);
            btnLeft.setCaption("New Buy");

            btnRight.setVisible(true);
            btnRight.setCaption("Save ticket");

        } else if (getStep() == 3) { //fail

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
        switch (getStep()) {
            case 1: //checkout    
                setStep(0);
                break;
            case 2: //success    
                setStep(0);
                break;
        }
    }

    private void handleStepRight() {
        switch (getStep()) {
            case 0: //begin 
                setStep(1);
                break;
            case 1: //checkout  
                setStep(2);
                break;
            case 3: //fail    
                setStep(0);
                break;
        }
    }
}
