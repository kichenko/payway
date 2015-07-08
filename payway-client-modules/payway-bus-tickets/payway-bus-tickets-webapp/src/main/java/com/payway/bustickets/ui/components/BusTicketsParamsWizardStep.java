/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.components;

import com.payway.commons.webapp.core.utils.NumberFormatConverterUtils;
import com.payway.bustickets.ui.components.containers.ChoiceDtoBeanContainer;
import com.payway.bustickets.ui.components.containers.DirectionDtoBeanContainer;
import com.payway.bustickets.ui.components.containers.RouteDtoBeanContainer;
import com.payway.bustickets.ui.components.containers.filters.RouteByDirectionFilter;
import com.payway.commons.webapp.ui.components.wizard.AbstractWizardStep;
import com.payway.messaging.model.bustickets.DirectionDto;
import com.payway.messaging.model.bustickets.RouteDto;
import com.payway.messaging.model.common.ChoiceDto;
import com.payway.messaging.model.common.CurrencyDto;
import com.payway.messaging.model.common.MoneyPrecisionDto;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * BusTicketsParamsWizardStep
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Getter
public final class BusTicketsParamsWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -3017619450081339095L;

    private static final String SUMMARY_LABEL_TEMPLATE = "Total %d ticket(s) x %s %s = %s %s";

    @UiField
    private TextField editContactNo;

    @UiField
    private ComboBox cbDirection;

    @UiField
    private ComboBox cbRoute;

    @UiField
    private ComboBox cbTripDate;

    @UiField
    private ComboBox cbBaggage;

    @UiField
    private Slider sliderQuantity;

    @UiField
    private Label lblSummary;

    @Setter
    private CurrencyDto currency;

    @Setter
    private MoneyPrecisionDto moneyPrecision;

    public BusTicketsParamsWizardStep() {
        init();
    }

    private void initValidators() {

        getEditContactNo().setValidationVisible(false);
        getCbDirection().setValidationVisible(false);
        getCbRoute().setValidationVisible(false);
        getCbTripDate().setValidationVisible(false);
        getCbBaggage().setValidationVisible(false);
        getSliderQuantity().setValidationVisible(false);

        getEditContactNo().addValidator(new RegexpValidator("[0-9]+", "Only digits allowed"));
        getEditContactNo().addValidator(new StringLengthValidator("Length is not equal 10", 10, 10, false));
        getEditContactNo().addValidator(new NullValidator("Empty number", false));
        getCbDirection().addValidator(new NullValidator("Empty direction", false));
        getCbRoute().addValidator(new NullValidator("Empty route", false));
        getCbTripDate().addValidator(new NullValidator("Empty trip date", false));
        getCbBaggage().addValidator(new NullValidator("Empty baggage", false));
        getSliderQuantity().addValidator(new DoubleRangeValidator("Invalid quantity", 1.0, Double.MAX_VALUE));
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsParamsWizardStep.xml", this));

        initValidators();

        getCbDirection().setItemCaptionPropertyId("name");
        getCbDirection().setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

        getCbRoute().setItemCaptionPropertyId("departureTime");
        getCbRoute().setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

        getCbTripDate().setItemCaptionPropertyId("label");
        getCbTripDate().setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

        getCbBaggage().setItemCaptionPropertyId("label");
        getCbBaggage().setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

        getCbDirection().setContainerDataSource(new DirectionDtoBeanContainer());
        getCbRoute().setContainerDataSource(new RouteDtoBeanContainer());
        getCbTripDate().setContainerDataSource(new ChoiceDtoBeanContainer());
        getCbBaggage().setContainerDataSource(new ChoiceDtoBeanContainer());

        //event change directions
        getCbDirection().addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -382717228031608542L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    DirectionDto direction = ((DirectionDtoBeanContainer) getCbDirection().getContainerDataSource()).getItem(event.getProperty().getValue()).getBean();
                    ((RouteDtoBeanContainer) getCbRoute().getContainerDataSource()).removeAllContainerFilters();
                    ((RouteDtoBeanContainer) getCbRoute().getContainerDataSource()).addContainerFilter(new RouteByDirectionFilter(direction));
                }

                getCbRoute().select(null);
                getCbTripDate().select(null);
                getCbBaggage().select(null);
                refreshSummary();
            }
        });

        //event change routes
        getCbRoute().addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -382717228031608542L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    RouteDto route = ((RouteDtoBeanContainer) getCbRoute().getContainerDataSource()).getItem(event.getProperty().getValue()).getBean();
                    if (route != null) {

                        if (route.getSeatsTotal() <= 0) {
                            getSliderQuantity().setMax(1.0);
                        } else {
                            getSliderQuantity().setMax(route.getSeatsTotal());
                        }

                        if (getSliderQuantity().getValue().intValue() > getSliderQuantity().getMax()) {
                            getSliderQuantity().setValue(1.0);
                        }
                    }
                }
                refreshSummary();
            }
        });

        //event change slider
        getSliderQuantity().addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -382717228031608542L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                refreshSummary();
            }
        });
    }

    public void setUp(List<DirectionDto> directions, List<RouteDto> routes, List<ChoiceDto> dates, List<ChoiceDto> baggages) {

        getEditContactNo().setValue("");
        getSliderQuantity().setMin(1.0);
        getSliderQuantity().setMax(100.0);
        getSliderQuantity().setValue(1.0);
        getLblSummary().setValue("");

        ((DirectionDtoBeanContainer) getCbDirection().getContainerDataSource()).addAll(directions);
        ((RouteDtoBeanContainer) getCbRoute().getContainerDataSource()).addAll(routes);
        ((ChoiceDtoBeanContainer) getCbTripDate().getContainerDataSource()).addAll(dates);
        ((ChoiceDtoBeanContainer) getCbBaggage().getContainerDataSource()).addAll(baggages);

        //set default values
        getCbDirection().select(((DirectionDtoBeanContainer) getCbDirection().getContainerDataSource()).getIdByIndex(0));
        getCbRoute().select(((RouteDtoBeanContainer) getCbRoute().getContainerDataSource()).getIdByIndex(0));
        getCbTripDate().select(((ChoiceDtoBeanContainer) getCbTripDate().getContainerDataSource()).getIdByIndex(0));
        getCbBaggage().select(((ChoiceDtoBeanContainer) getCbBaggage().getContainerDataSource()).getIdByIndex(0));
    }

    @Override
    public boolean validate() {

        boolean ok = true;

        try {

            getEditContactNo().validate();
            getCbDirection().validate();
            getCbRoute().validate();
            getCbTripDate().validate();
            getCbBaggage().validate();
            getSliderQuantity().validate();

        } catch (Exception ex) {

            getEditContactNo().setValidationVisible(true);
            getCbDirection().setValidationVisible(true);
            getCbRoute().setValidationVisible(true);
            getCbTripDate().setValidationVisible(true);
            getCbBaggage().setValidationVisible(true);
            getSliderQuantity().setValidationVisible(true);

            ok = false;
        }

        UI.getCurrent().push();

        return ok;
    }

    public int getQuantity() {
        return getSliderQuantity().getValue().intValue();
    }

    public String getContactNo() {
        return getEditContactNo().getValue();
    }

    public DirectionDto getDirection() {

        if (getCbDirection().getContainerDataSource() != null) {
            DirectionDtoBeanContainer container = (DirectionDtoBeanContainer) getCbDirection().getContainerDataSource();
            BeanItem<DirectionDto> beanItem = container.getItem(getCbDirection().getValue());
            if (beanItem != null) {
                return beanItem.getBean();
            }
        }

        return null;
    }

    public ChoiceDto getBaggage() {

        if (getCbBaggage().getContainerDataSource() != null) {
            ChoiceDtoBeanContainer container = (ChoiceDtoBeanContainer) getCbBaggage().getContainerDataSource();
            BeanItem<ChoiceDto> beanItem = container.getItem(getCbBaggage().getValue());
            if (beanItem != null) {
                return beanItem.getBean();
            }
        }

        return null;
    }

    public RouteDto getRoute() {

        if (getCbRoute().getContainerDataSource() != null) {
            RouteDtoBeanContainer container = (RouteDtoBeanContainer) getCbRoute().getContainerDataSource();
            BeanItem<RouteDto> beanItem = container.getItem(getCbRoute().getValue());
            if (beanItem != null) {
                return beanItem.getBean();
            }
        }

        return null;
    }

    public ChoiceDto getTripDate() {

        if (getCbTripDate().getContainerDataSource() != null) {
            ChoiceDtoBeanContainer container = (ChoiceDtoBeanContainer) getCbTripDate().getContainerDataSource();
            BeanItem<ChoiceDto> beanItem = container.getItem(getCbTripDate().getValue());
            if (beanItem != null) {
                return beanItem.getBean();
            }
        }

        return null;
    }

    public void refreshSummary() {

        RouteDto route = getRoute();
        if (route != null) {

            double price = route.getPrice();
            double sum = getQuantity() * route.getPrice();

            String strCurrency = "";
            String strPrice = Double.toString(price);
            String strSum = Double.toString(sum);

            if (getCurrency() != null) {
                strCurrency = getCurrency().getIso();
            } else {
                log.error("Empty currency");
            }

            if (getMoneyPrecision() != null) {
                if (MoneyPrecisionDto.Auto.equals(getMoneyPrecision())) {

                    if (com.payway.commons.webapp.core.utils.NumberUtils.isInteger(price)) {
                        strPrice = NumberFormatConverterUtils.format(price, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
                    } else {
                        strPrice = NumberFormatConverterUtils.format(price, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
                    }

                    if (com.payway.commons.webapp.core.utils.NumberUtils.isInteger(sum)) {
                        strSum = NumberFormatConverterUtils.format(sum, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
                    } else {
                        strSum = NumberFormatConverterUtils.format(sum, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
                    }

                } else if (MoneyPrecisionDto.Zero.equals(getMoneyPrecision())) {
                    strPrice = NumberFormatConverterUtils.format(price, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
                    strSum = NumberFormatConverterUtils.format(sum, NumberFormatConverterUtils.DEFAULT_PATTERN_WITHOUT_DECIMALS);
                } else if (MoneyPrecisionDto.Two.equals(getMoneyPrecision())) {
                    strPrice = NumberFormatConverterUtils.format(price, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
                    strSum = NumberFormatConverterUtils.format(sum, NumberFormatConverterUtils.DEFAULT_PATTERN_WITH_DECIMALS);
                } else {
                    log.error("Empty money precision");
                }

                getLblSummary().setValue(String.format(SUMMARY_LABEL_TEMPLATE, getQuantity(), strPrice, strCurrency, strSum, strCurrency));

            } else {
                getLblSummary().setValue("");
            }
        }
    }
}
