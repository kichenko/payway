/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui.view.core;

import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.vaadin.server.Resource;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.MenuBar;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * AbstractMainView
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
public abstract class AbstractMainView extends CustomComponent implements CustomComponentInitialize, SideBarMenu.SideBarMenuItemClickListener {

    public interface SlideBarMenuItemClickCallback {

        void onClick(SideBarMenu.MenuItem menuItem);
    }

    public interface ViewActivateStateChangeListener {

        void onActivate(WorkspaceView workspaceView);

        void onDeactivate(WorkspaceView workspaceView);
    }

    @Getter
    @AllArgsConstructor
    public static class UserMenuItem implements Serializable {

        private static final long serialVersionUID = 8581718071618173495L;

        private final String caption;
        private final Resource icon;
        private final MenuBar.Command command;
        private final boolean addSeparator;
    }

    @Autowired
    @Qualifier(value = "viewFactory")
    protected ViewFactory viewFactory;

    @UiField
    protected CssLayout mainViewLayout;

    @UiField
    @Getter
    protected MenuBar userMenu;

    @UiField
    @Getter
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

    protected SlideBarMenuItemClickCallback sbMenuButtonClickCallback;

    @Getter
    @Setter
    protected ViewActivateStateChangeListener viewActivateStateChangeListener;

    /**
     * Create user menu
     *
     * @param caption
     * @param icon
     * @param items
     */
    public void initializeUserMenu(String caption, Resource icon, List<UserMenuItem> items) {
        MenuBar.MenuItem settingsItem = userMenu.addItem(caption, icon, null);
        for (UserMenuItem item : items) {
            if (item.isAddSeparator()) {
                settingsItem.addItem(item.getCaption(), item.getIcon(), item.getCommand());
                settingsItem.addSeparator();
            } else {
                settingsItem.addItem(item.getCaption(), item.getIcon(), item.getCommand());
            }
        }
    }

    /**
     * Create sideBar menu
     *
     * @param items
     * @param sbButtonclick
     */
    public void initializeSideBarMenu(Collection<SideBarMenu.MenuItem> items, SlideBarMenuItemClickCallback sbButtonclick) {
        for (SideBarMenu.MenuItem i : items) {
            sideBarMenu.addMenuItem(i, this);
        }

        sbMenuButtonClickCallback = sbButtonclick;
    }

    /**
     * Sidebar menu click listener
     *
     * @param menuItem
     */
    @Override
    public void onClickSideBarMenuItem(SideBarMenu.MenuItem menuItem) {

        com.vaadin.ui.Component view = viewFactory.view(menuItem.getViewId());
        WorkspaceView newView = (WorkspaceView) view;

        if (sbMenuButtonClickCallback != null) {
            sbMenuButtonClickCallback.onClick(menuItem);
        }

        if (panelContent.getComponentCount() == 1) {
            WorkspaceView oldView = (WorkspaceView) panelContent.getComponent(0);
            if (viewActivateStateChangeListener != null && oldView != null) {
                viewActivateStateChangeListener.onDeactivate(oldView);
            }
        }

        panelContent.removeAllComponents();
        panelContent.addComponent(view);

        if (newView != null) {
            newView.setMenuBar(menuBar);
            newView.setSideBarMenu(sideBarMenu);
            setUpCustomWorkspaceView(newView);

            newView.activate();
            if (viewActivateStateChangeListener != null) {
                viewActivateStateChangeListener.onActivate(newView);
            }
        }
    }

    public void clearWorkspaceView() {

        if (panelContent.getComponentCount() == 1) {
            WorkspaceView oldView = (WorkspaceView) panelContent.getComponent(0);
            if (viewActivateStateChangeListener != null && oldView != null) {
                viewActivateStateChangeListener.onDeactivate(oldView);
            }
        }

        panelContent.removeAllComponents();
    }

    /**
     * Ovveride in child class and set up custom workspace view
     *
     * @param workspaceView
     */
    public abstract void setUpCustomWorkspaceView(WorkspaceView workspaceView);
}
