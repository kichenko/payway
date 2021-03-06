/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BreadCrumbs
 *
 * Panel panel = new Panel();
 * BreadCrumbs breadCrumbs = new BreadCrumbs();
 *
 * breadCrumbs.setWidth(100.0f, Unit.PERCENTAGE);
 * breadCrumbs.addCrumb("", FontAwesome.HOME);
 * breadCrumbs.setCrumbEnabled(0, false);
 *
 * breadCrumbs.addBreadCrumbSelectListener(newBreadCrumbs.BreadCrumbSelectListener() {
 *     @Override public void selected(int index) {
 *         Notification.show("selected crumb is - " + index + " with state - " + breadCrumbs.getCrumbState(index));
 *     }
 * });
 *
 * for (int i = 0; i * <10; i++) {
 *     breadCrumbs.addCrumb("Item" + i, FontAwesome.FOLDER, "Item" + i);
 * }
 *
 * breadCrumbs.selectCrumb(breadCrumbs.size() - 1, false);
 * panel.setContent(breadCrumbs);
 *
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public class BreadCrumbs extends HorizontalLayout {

    public interface BreadCrumbSelectListener extends Serializable {

        void selected(int index);
    }

    private final List<Object> crumbStates = new ArrayList<>();
    private MenuBar menuBar = new MenuBar();
    MenuBar.MenuItem previousSelectedCrumb = null;
    private BreadCrumbSelectListener selectListener;

    private final MenuBar.Command menuCommand = new MenuBar.Command() {

        @Override
        public void menuSelected(final MenuBar.MenuItem selectedItem) {
            selectCrumb(menuBar.getItems().indexOf(selectedItem), true);
        }
    };

    public BreadCrumbs() {
        menuBar.setSizeFull();
        addComponent(menuBar);
    }

    private boolean handleSelectCrumb(int index) {
        if (index >= 0 && index < menuBar.getItems().size()) {
            if ((index + 1) < menuBar.getItems().size()) {
                menuBar.getItems().subList(index + 1, menuBar.getItems().size()).clear();
                crumbStates.subList(index + 1, crumbStates.size()).clear();
                menuBar.markAsDirty();
            }
            return true;
        }
        return false;
    }

    @Override
    public void addStyleName(String style) {
        menuBar.addStyleName(style);
    }

    public void addCrumb(String caption) {
        menuBar.addItem(caption, menuCommand);
        crumbStates.add(null);
    }

    public void addCrumb(String caption, Resource icon) {
        menuBar.addItem(caption, icon, menuCommand);
        crumbStates.add(null);
    }

    public void addCrumb(String caption, Resource icon, Object crumbState) {
        menuBar.addItem(caption, icon, menuCommand);
        crumbStates.add(crumbState);
    }

    public boolean setCrumbEnabled(int index, boolean isEnabled) {
        if (index >= 0 && index < menuBar.getItems().size()) {
            menuBar.getItems().get(index).setEnabled(isEnabled);
            menuBar.markAsDirty();
            return true;
        }
        return false;
    }

    public Object getCrumbState(int index) {
        if (index >= 0 && index < crumbStates.size()) {
            return crumbStates.get(index);
        }
        return null;
    }

    public void setCrumbState(int index, Object state) {
        if (index >= 0 && index < crumbStates.size()) {
            crumbStates.set(index, state);
        }
    }

    public int size() {
        return menuBar.getItems().size();
    }

    public void addBreadCrumbSelectListener(BreadCrumbSelectListener listener) {
        selectListener = listener;
    }

    public void removeBreadCrumbSelectListener() {
        selectListener = null;
    }

    public boolean removeCrumb(int index) {
        if (index >= 0 && index < menuBar.getItems().size()) {
            menuBar.getItems().remove(index);
            crumbStates.remove(index);
            menuBar.markAsDirty();
            return true;
        }
        return false;
    }

    public boolean selectCrumb(int index, boolean fireSelectEvent) {
        if (index >= 0 && index < menuBar.getItems().size()) {
            final MenuBar.MenuItem selectedItem = menuBar.getItems().get(index);

            if (previousSelectedCrumb != null) {
                previousSelectedCrumb.setStyleName(null);
            }

            selectedItem.setStyleName("highlight");
            previousSelectedCrumb = selectedItem;
            if (handleSelectCrumb(index)) {
                if (fireSelectEvent && selectListener != null) {
                    selectListener.selected(index);
                }
                return true;
            }
        }
        return false;
    }
}
