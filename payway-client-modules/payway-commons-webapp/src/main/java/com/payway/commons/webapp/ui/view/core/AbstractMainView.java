/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.MenuBar;
import java.util.Collection;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AbstractMainView
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
public abstract class AbstractMainView extends CustomComponent implements CustomComponentInitialize, SideBarMenu.SideBarMenuButton.SideBarMenuButtonClickListener {

    public interface SlideBarMenuButtonClickCallback {

        void onClick(SideBarMenu.SideBarMenuButton button, Button.ClickEvent event);
    }

    @Autowired
    @Qualifier(value = "viewFactory")
    protected ViewFactory viewFactory;

    @UiField
    protected CssLayout mainViewLayout;

    @UiField
    protected MenuBar userMenu;

    @UiField
    protected MenuBar menuBar;

    @UiField
    @Getter
    protected SideBarMenu sideBarMenu;

    @UiField
    protected CssLayout panelContent;

    @UiField
    protected HorizontalSplitPanel splitHorizontalPanel;

    @UiField
    protected CssLayout layoutLeft;

    @UiField
    protected CssLayout layoutRight;

    protected SlideBarMenuButtonClickCallback sbMenuButtonClickCallback;

    /**
     * Create user menu
     *
     * @param caption
     * @param icon
     * @param items
     */
    public void initializeUserMenu(String caption, Resource icon, Collection<ImmutableTriple<String, Resource, MenuBar.Command>> items) {
        MenuBar.MenuItem settingsItem = userMenu.addItem(caption, icon, null);
        for (Triple<String, Resource, MenuBar.Command> i : items) {
            settingsItem.addItem(i.getLeft(), i.getMiddle(), i.getRight());
        }
    }

    /**
     * Create sideBar menu
     *
     * @param items
     * @param sbButtonclick
     */
    public void initializeSideBarMenu(Collection<SideBarMenu.MenuItem> items, SlideBarMenuButtonClickCallback sbButtonclick) {
        for (SideBarMenu.MenuItem i : items) {
            sideBarMenu.addMenuItem(i, this);
        }

        sbMenuButtonClickCallback = sbButtonclick;
    }

    /**
     * Sidebar menu click listener
     *
     * @param button
     * @param event
     */
    @Override
    public void onClickSideBarMenuItemButton(SideBarMenu.SideBarMenuButton button, Button.ClickEvent event) {

        if (sbMenuButtonClickCallback != null) {
            sbMenuButtonClickCallback.onClick(button, event);
        }

        panelContent.removeAllComponents();

        com.vaadin.ui.Component view = (com.vaadin.ui.Component) viewFactory.view(button.getTag());
        panelContent.addComponent(view);

        WorkspaceView v = (WorkspaceView) view;
        if (v != null) {
            v.setMenuBar(menuBar);
            setUpCustomWorkspaceView(v);
            v.activate();
        }
    }

    /**
     * Ovveride in child class and set up custom workspace view
     *
     * @param workspaceView
     */
    public abstract void setUpCustomWorkspaceView(WorkspaceView workspaceView);
}
