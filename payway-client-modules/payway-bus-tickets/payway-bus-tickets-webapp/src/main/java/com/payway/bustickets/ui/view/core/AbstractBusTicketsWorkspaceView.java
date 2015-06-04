/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.ui.view.core;

import com.payway.commons.webapp.ui.view.core.WorkspaceView;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * AbstractWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public abstract class AbstractBusTicketsWorkspaceView extends VerticalLayout implements WorkspaceView {

    protected MenuBar menuBar;

    @Override
    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    @Override
    public MenuBar getMenuBar() {
        return menuBar;
    }
}
