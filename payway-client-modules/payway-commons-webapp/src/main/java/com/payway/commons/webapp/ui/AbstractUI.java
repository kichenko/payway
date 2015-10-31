/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui;

import com.payway.commons.webapp.core.CommonAttributes;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.components.ProgressBarWindow;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * AbstractUI
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
@Slf4j
public abstract class AbstractUI extends UI implements InteractionUI {

    private static final long serialVersionUID = 823360792811823114L;

    @Getter
    @Setter
    @Autowired
    @Qualifier(value = "sessionEventBus")
    protected SessionEventBus sessionEventBus;

    private final ProgressBarWindow progressBarWindow = new ProgressBarWindow();
    private int progressCounter;

    protected void registerDetach() {

        addDetachListener(new DetachListener() {
            private static final long serialVersionUID = -327258454602850406L;

            @Override
            public void detach(DetachEvent event) {
                cleanUpOnDetach();
            }
        });
    }

    protected void cleanUpOnDetach() {
        unSubscribeSessionEventBus();
    }

    protected void subscribeSessionEventBus() {
        getSessionEventBus().addSubscriber(this);
    }

    protected void unSubscribeSessionEventBus() {
        getSessionEventBus().removeSubscriber(this);
    }

    @Override
    public void showNotification(String title, String message, Notification.Type kind) {

        if (Notification.Type.ERROR_MESSAGE.equals(kind)) {
            Notification notification = new Notification(message, kind);
            notification.setPosition(Position.MIDDLE_CENTER);
            notification.setStyleName("closable error");
            notification.show(Page.getCurrent());
        } else {
            Notification.show(message, kind);
        }

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

    protected abstract List<SideBarMenu.MenuItem> getSideBarMenuItems();

    protected List<AbstractMainView.UserMenuItem> getMenuBarItems() {
        return Collections.singletonList(
                new AbstractMainView.UserMenuItem("Sign Out", new ThemeResource("images/user_menu_item_logout.png"), new MenuBar.Command() {
                    private static final long serialVersionUID = 7160936162824727503L;

                    @Override
                    public void menuSelected(final MenuBar.MenuItem selectedItem) {
                        for (final UI ui : VaadinSession.getCurrent().getUIs()) {
                            ui.access(new Runnable() {
                                @Override
                                public void run() {
                                    //#hack cookie
                                    URI uri = UI.getCurrent().getPage().getLocation();
                                    UI.getCurrent().getPage().getJavaScript().execute("document.cookie='" + CommonAttributes.REMEMBER_ME.value() + "=;" + "; path=" + uri.getPath() + " ;expires=Thu, 01-Jan-1970 00:00:01 GMT;'");
                                    ui.getPage().reload();
                                }
                            });
                        }
                        VaadinSession.getCurrent().close();
                    }
                }, false));
    }

    @Override
    public MessageBox showMessageBox(String title, String message, Icon icon, MessageBoxListener listener, ButtonId... buttonIds) {
        return MessageBox.showPlain(Icon.INFO, title, message, listener, buttonIds);
    }

    @Override
    public Cookie getCookieByName(String name) {

        if (VaadinService.getCurrentRequest() == null) {
            log.debug("Bad get cookie with name [{}], because vaadin current request is null", name);
            return null;
        }

        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
}
