/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.vaadin.ui.MenuBar;

/**
 * WorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public interface WorkspaceView {

    void activate();

    void setMenuBar(MenuBar menuBar);

    MenuBar getMenuBar();

    void setSideBarMenu(SideBarMenu sideBarMenu);

    SideBarMenu getSideBarMenu();
}
