/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components;

import com.payway.bustickets.core.utils.NumberFormatConverterUtils;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.messaging.model.bustickets.DirectionDto;
import com.payway.messaging.model.bustickets.RouteDto;
import com.payway.messaging.model.common.ChoiceDto;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import lombok.Getter;
import lombok.Setter;
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
public final class BusTicketsConfirmWizardStep extends AbstractWizardStep {

    public static final int STEP_NO = 1;

    private static final long serialVersionUID = -4226906157266350856L;

    private static final String TOTAL_COST_WITH_DISCOUNT_LABEL_TEMPLATE = "%s %s (discounted: %s %s)";
    private static final String TOTAL_COST_WITHOUT_DISCOUNT_LABEL_TEMPLATE = "%s %s";

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

    @Setter
    private CurrencyDto currency;

    @Setter
    private MoneyPrecisionDto moneyPrecision;

    @Setter
    private RouteDto route;

    @Setter
    private int quantity;

    @Setter
    private double totalCost;

    @Setter
    private boolean hasDiscount;

    public BusTicketsConfirmWizardStep() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsConfirmWizardStep.xml", this));
    }

    public void setRouteName(String routeName) {
        lbRouteName.setValue(routeName);
    }

    public void setUp(final String routeName, final String contactNo, final DirectionDto direction, final RouteDto route, final ChoiceDto tripDate, final ChoiceDto baggage, final int quantity, final double totalCost, final boolean hasDiscount) {

        setRoute(route);
        setQuantity(quantity);
        setTotalCost(totalCost);
        setHasDiscount(hasDiscount);

        getLbRouteName().setValue(routeName);
        getEditContactNo().setValue(contactNo);
        getEditDirection().setValue(direction.getName());
        getEditRoute().setValue(route.getDepartureTime());
        getEditTripDate().setValue(tripDate.getLabel());
        getEditBaggage().setValue(baggage.getLabel());
        getEditQuantity().setValue(Integer.toString(quantity));

        refreshTotalCost();
    }

    public void refreshTotalCost() {

        if (route == null) {
            getEditTotalCost().setValue("");
            return;
        }

        double discount = totalCost - (quantity * route.getPrice());

        String strCurrency = "";
        String strTotalCost = Double.toString(totalCost);
        String strDiscount = Double.toString(discount);

        if (getCurrency() != null) {
            strCurrency = getCurrency().getIso();
        } else {
            log.error("Empty currency");
        }

        if (getMoneyPrecision() != null) {
            if (MoneyPrecisionDto.Auto.equals(getMoneyPrecision())) {

                if (com.payway.bustickets.core.utils.NumberUtils.isInteger(discount)) {
                    strDiscount = NumberFormatConverterUtils.format(discount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
                } else {
                    strDiscount = NumberFormatConverterUtils.format(discount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
                }

                if (com.payway.bustickets.core.utils.NumberUtils.isInteger(totalCost)) {
                    strTotalCost = NumberFormatConverterUtils.format(totalCost, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
                } else {
                    strTotalCost = NumberFormatConverterUtils.format(totalCost, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
                }

            } else if (MoneyPrecisionDto.Zero.equals(getMoneyPrecision())) {
                strDiscount = NumberFormatConverterUtils.format(discount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
                strTotalCost = NumberFormatConverterUtils.format(totalCost, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
            } else if (MoneyPrecisionDto.Two.equals(getMoneyPrecision())) {
                strDiscount = NumberFormatConverterUtils.format(discount, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
                strTotalCost = NumberFormatConverterUtils.format(totalCost, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
            } else {
                log.error("Empty money precision");
            }

            if (hasDiscount) {
                getEditTotalCost().setValue(String.format(TOTAL_COST_WITH_DISCOUNT_LABEL_TEMPLATE, strTotalCost, strCurrency, strDiscount, strCurrency));
            } else {
                getEditTotalCost().setValue(String.format(TOTAL_COST_WITHOUT_DISCOUNT_LABEL_TEMPLATE, strTotalCost, strCurrency));
            }
        } else {
            getEditTotalCost().setValue("");
        }
    }
}
