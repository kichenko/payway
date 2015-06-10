/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;

/**
 * AbstractWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public abstract class AbstractWorkspaceView extends VerticalLayout implements WorkspaceView {

    protected MenuBar menuBar;
    protected SideBarMenu sideBarMenu;

    @Override
    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    @Override
    public MenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public void setSideBarMenu(SideBarMenu sideBarMenu) {
        this.sideBarMenu = sideBarMenu;
    }

    @Override
    public SideBarMenu getSideBarMenu() {
        return sideBarMenu;
    }
}
