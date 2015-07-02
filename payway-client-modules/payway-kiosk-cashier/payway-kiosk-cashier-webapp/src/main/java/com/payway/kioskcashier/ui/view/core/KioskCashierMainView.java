/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.kioskcashier.ui.view.core;

import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.vaadin.server.Sizeable;
import com.vaadin.spring.annotation.UIScope;
import javax.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;

/**
 * KioskCashierMainView
 *
 * @author Sergey Kichenko
 * @created 02.07.15 00:00
 */
@UIScope
@Component
@NoArgsConstructor
public class KioskCashierMainView extends AbstractMainView {

    private static final float SIDEBAR_DEFAULT_WIDTH_PERCENT = 20;

    private static final long serialVersionUID = -3129629793062652278L;

    @PostConstruct
    void postConstruct() {

        setSizeFull();
        setCompositionRoot(Clara.create("KioskCashierMainView.xml", this));

        splitHorizontalPanel.setFirstComponent(layoutLeft);
        splitHorizontalPanel.setSecondComponent(layoutRight);
        splitHorizontalPanel.setSplitPosition(SIDEBAR_DEFAULT_WIDTH_PERCENT, Sizeable.Unit.PERCENTAGE);
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
