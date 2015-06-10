/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.view.workspace;

import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import com.vaadin.spring.annotation.UIScope;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;

/**
 * BusTicketsEmptyWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@UIScope
@Component(value = "bus-ticket-empty-workspace-view")
public class BusTicketsEmptyWorkspaceView extends AbstractBusTicketsWorkspaceView {

    private static final long serialVersionUID = 4111225080130198518L;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addComponent(Clara.create("BusTicketsEmptyWorkspaceView.xml", this));
    }

    @Override
    public void activate() {
        //
    }
}
