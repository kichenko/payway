/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.workspace.airportexpress;

import com.payway.bustickets.ui.components.airportexpress.BusTicketsWizard;
import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import com.vaadin.spring.annotation.UIScope;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AirportExpressWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@UIScope
@Component(value = "airport-express-bus-tickets-workspace-view")
public class AirportExpressWorkspaceView extends AbstractBusTicketsWorkspaceView {

    private static final long serialVersionUID = 4644909330724328135L;

    @UiField
    private BusTicketsWizard wizard;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("AirportExpressWorkspaceView.xml", this));
    }

    @Override
    public void activate() {
        //
    }
}
