/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.airportexpress.ui.components;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
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
public class BusTicketsSuccessWizardStep extends AbstractWizardStep {

    private static final long serialVersionUID = 9042170733083703085L;

    @UiField
    private Image image;

    public BusTicketsSuccessWizardStep() {
        init();
    }

    private void init() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsSuccessWizardStep.xml", this));
    }

    public void setSource(Resource resource) {
        image.setSource(resource);
    }

    public Resource getSource() {
        return image.getSource();
    }

}
