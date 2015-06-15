/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.payway.bustickets.airportexpress.ui.components.containers.ChoiceDtoBeanContainer;
import com.payway.bustickets.airportexpress.ui.components.containers.DirectionDtoBeanContainer;
import com.payway.bustickets.airportexpress.ui.components.containers.RouteDtoBeanContainer;
import com.payway.bustickets.airportexpress.ui.components.containers.filters.RouteByDirectionFilter;
import com.payway.messaging.model.bustickets.DirectionDto;
import com.payway.messaging.model.bustickets.RouteDto;
import com.payway.messaging.model.common.ChoiceDto;
import com.vaadin.data.Property;
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
public class BusTicketsParamsWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = -3017619450081339095L;

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
    private Label lblSlider;

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

    private void init() {
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
                getSliderQuantity().setValue(0.0);
                getLblSlider().setValue(Integer.toString(getSliderQuantity().getValue().intValue()));
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
                        getSliderQuantity().setMin(0.0);
                        getSliderQuantity().setMax(route.getSeatsTotal());
                        getSliderQuantity().setValue(0.0);
                        getLblSlider().setValue(Integer.toString(getSliderQuantity().getValue().intValue()));
                    }
                }
            }
        });

        //event change slider
        getSliderQuantity().addValueChangeListener(new Property.ValueChangeListener() {
            private static final long serialVersionUID = -382717228031608542L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                getLblSlider().setValue(Integer.toString(getSliderQuantity().getValue().intValue()));
            }
        });
    }

    public void setUp(List<DirectionDto> directions, List<RouteDto> routes, List<ChoiceDto> dates, List<ChoiceDto> baggages) {

        getEditContactNo().setValue("");
        getSliderQuantity().setMin(0.0);
        getSliderQuantity().setMax(100.0);
        getSliderQuantity().setValue(0.0);
        getLblSlider().setValue(Integer.toString(getSliderQuantity().getValue().intValue()));

        ((DirectionDtoBeanContainer) getCbDirection().getContainerDataSource()).addAll(directions);
        ((RouteDtoBeanContainer) getCbRoute().getContainerDataSource()).addAll(routes);
        ((ChoiceDtoBeanContainer) getCbTripDate().getContainerDataSource()).addAll(dates);
        ((ChoiceDtoBeanContainer) getCbBaggage().getContainerDataSource()).addAll(baggages);
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
        return ((DirectionDtoBeanContainer) getCbDirection().getContainerDataSource()).getItem(getCbDirection().getValue()).getBean();
    }

    public ChoiceDto getBaggage() {
        return ((ChoiceDtoBeanContainer) getCbBaggage().getContainerDataSource()).getItem(getCbBaggage().getValue()).getBean();
    }

    public RouteDto getRoute() {
        return ((RouteDtoBeanContainer) getCbRoute().getContainerDataSource()).getItem(getCbRoute().getValue()).getBean();
    }

    public ChoiceDto getTripDate() {
        return ((ChoiceDtoBeanContainer) getCbTripDate().getContainerDataSource()).getItem(getCbTripDate().getValue()).getBean();
    }

}
