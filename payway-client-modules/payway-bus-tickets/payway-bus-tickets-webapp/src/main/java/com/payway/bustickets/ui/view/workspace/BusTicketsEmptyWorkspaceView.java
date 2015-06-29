/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.view.workspace;

import com.payway.bustickets.ui.view.core.AbstractBusTicketsWorkspaceView;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;

/**
 * BusTicketsEmptyWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 08.06.15 00:00
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component(value = BusTicketsEmptyWorkspaceView.BUS_TICKETS_EMPTY_WORKSPACE_VIEW_ID)
public class BusTicketsEmptyWorkspaceView extends AbstractBusTicketsWorkspaceView {

    public static final String BUS_TICKETS_EMPTY_WORKSPACE_VIEW_ID = WORKSPACE_VIEW_ID_PREFIX + "empty";

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
