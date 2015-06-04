/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.view.core;

import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.vaadin.spring.annotation.UIScope;
import javax.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;

/**
 * Главное окно
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@UIScope
@Component
@NoArgsConstructor
public class BusTicketsMainView extends AbstractMainView {

    private static final long serialVersionUID = 3469218271943273963L;

    private static final float SIDEBAR_DEFAULT_WIDTH_PERCENT = 20;

    @PostConstruct
    void init() {
        
        setSizeFull();
        setCompositionRoot(Clara.create("BusTicketsMainView.xml", this));

        splitHorizontalPanel.setFirstComponent(layoutLeft);
        splitHorizontalPanel.setSecondComponent(layoutRight);
        splitHorizontalPanel.setSplitPosition(SIDEBAR_DEFAULT_WIDTH_PERCENT, Unit.PERCENTAGE);
    }

    @Override
    public void initialize() {
        //
    }

    @Override
    public void setUpCustomWorkspaceView(WorkspaceView workspaceView) {
        //
    }
}
