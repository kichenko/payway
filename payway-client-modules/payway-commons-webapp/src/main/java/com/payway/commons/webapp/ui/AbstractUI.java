/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui;

import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.components.ProgressBarWindow;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang3.tuple.ImmutableTriple;

/**
 * AbstractUI
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public abstract class AbstractUI extends UI implements InteractionUI {

    public abstract SessionEventBus getSessionEventBus();

    private final ProgressBarWindow progressBarWindow = new ProgressBarWindow();

    private int progressCounter;

    @Override
    public void showNotification(String caption, String text, Notification.Type kind) {
        Notification.show(text, kind);
        UI.getCurrent().push();
    }

    @Override
    public void showProgressBar() {
        progressCounter += 1;
        if (!progressBarWindow.isAttached()) {
            progressBarWindow.show();
            UI.getCurrent().push();
        }
    }

    @Override
    public void closeProgressBar() {
        progressCounter -= 1;
        if (progressBarWindow.isAttached() && progressCounter == 0) {
            progressBarWindow.close();
            UI.getCurrent().push();
        }
    }

    protected abstract Collection<SideBarMenu.MenuItem> getSideBarMenuItems();

    protected Collection<ImmutableTriple<String, Resource, MenuBar.Command>> getMenuBarItems() {
        return Collections.singletonList(
                new ImmutableTriple<String, Resource, MenuBar.Command>("Sign Out", new ThemeResource("images/user_menu_item_logout.png"), new MenuBar.Command() {
                    private static final long serialVersionUID = 7160936162824727503L;

                    @Override
                    public void menuSelected(final MenuBar.MenuItem selectedItem) {
                        VaadinSession.getCurrent().close();
                        UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
                        VaadinSession.getCurrent().close();
                        Page.getCurrent().reload();
                    }
                }));
    }
}
