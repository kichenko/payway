/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.view.workspace.bustickets;

import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import com.vaadin.spring.annotation.UIScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * BusTicketsWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 22.04.15 00:00
 */
@Slf4j
@UIScope
@Component(value = "bus-tickets")
public class BusTicketsWorkspaceView extends AbstractBusTicketsWorkspaceView {
    
    private static final long serialVersionUID = 4111225080130198518L;

    @Override
    public void activate() {
        //
    }
}
