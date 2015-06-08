/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components.airportexpress;

import com.vaadin.ui.Button;
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

    @UiField
    private Button btnLeft;
    @UiField
    private Button btnRight;

    @UiField
    private VerticalLayout layoutContent;

    public BusTicketsWizard() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsWizard.xml", this));

        setStep(0);
        getSteps().add(new BusTicketsParamsWizardStep());
        getSteps().add(new BusTicketsConfirmWizardStep());

        layoutContent.removeAllComponents();
        layoutContent.addComponent(getSteps().get(step));

        btnLeft.setVisible(false);
        btnLeft.setCaption("Back");

        btnRight.setVisible(true);
        btnRight.setCaption("Next");
        
        //
        btnLeft.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (getStep() == 1) {
                    setStep(0);
                    layoutContent.removeAllComponents();
                    layoutContent.addComponent(getSteps().get(step));
                    btnLeft.setVisible(false);
                    btnRight.setCaption("Next");
                }
            }
        });

        //
        btnRight.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (getStep() == 0) {
                    setStep(1);
                    layoutContent.removeAllComponents();
                    layoutContent.addComponent(getSteps().get(step));
                    btnLeft.setVisible(true);
                    btnRight.setVisible(true);
                    btnRight.setCaption("Checkout");
                }
            }
        });
    }
}
