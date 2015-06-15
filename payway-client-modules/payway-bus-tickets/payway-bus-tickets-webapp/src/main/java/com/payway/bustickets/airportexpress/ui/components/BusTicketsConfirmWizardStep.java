/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import java.text.DecimalFormat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BusTicketsConfirmWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Getter
public class BusTicketsConfirmWizardStep extends AbstractWizardStep {

    public static final int STEP_NO = 1;

    private static final long serialVersionUID = -4226906157266350856L;

    @UiField
    private TextField editContactNo;

    @UiField
    private TextField editDirection;

    @UiField
    private TextField editTripDate;

    @UiField
    private TextField editRoute;

    @UiField
    private TextField editBaggage;

    @UiField
    private TextField editQuantity;

    @UiField
    private TextField editTotalCost;

    @UiField
    private Label lbRouteName;
    
    public BusTicketsConfirmWizardStep() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsConfirmWizardStep.xml", this));
    }

    public void setContactNo(String value) {
        getEditContactNo().setValue(value);
    }

    public void setDirection(String direction) {
        getEditDirection().setValue(direction);
    }

    public void setRoute(String route) {
        getEditRoute().setValue(route);
    }

    public void setTripDate(String tripDate) {
        getEditTripDate().setValue(tripDate);
    }

    public void setBaggage(String baggage) {
        getEditBaggage().setValue(baggage);
    }

    public void setQuantity(int quantity) {
        getEditQuantity().setValue(Integer.toString(quantity));
    }

    public void setTotalCost(Double totalCost) {
        getEditTotalCost().setValue(new DecimalFormat("###.##").format(totalCost));
    }

    public void setHasDiscount(boolean hasDiscount) {
        /*
        if (hasDiscount) {
            editTotalCost.setCaption("Total cost (with discount)");
        } else {
            editTotalCost.setCaption("Total cost (without discount)");
        }*/
    }
    
    public void setRouteName(String routeName) {
        lbRouteName.setValue(routeName);
    }
}
