/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.ui;

import com.google.common.eventbus.Subscribe;
import com.payway.commons.webapp.bus.event.ConnectedClientAppEventBus;
import com.payway.commons.webapp.bus.event.DisconnectedClientAppEventBus;
import com.payway.commons.webapp.ui.bus.SessionEventBus;
import com.payway.commons.webapp.ui.components.ProgressBarWindow;
import com.payway.commons.webapp.ui.components.SideBarMenu;
import com.payway.commons.webapp.ui.view.core.AbstractMainView;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
        if (progressCounter < 0) {
            progressCounter = 0;
        }

        if (progressBarWindow.isAttached() && progressCounter == 0) {
            progressBarWindow.close();
            UI.getCurrent().push();
        }
    }

    protected abstract List<SideBarMenu.MenuItem> getSideBarMenuItems();

    protected abstract List<AbstractMainView.UserMenuItem> getMenuBarItems();

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

    @Subscribe
    public void onConnectedClientAppEventBus(ConnectedClientAppEventBus event) {
        ((InteractionUI) UI.getCurrent()).showNotification("", "Connected", Notification.Type.TRAY_NOTIFICATION);
    }

    @Subscribe
    public void onDisconnectedClientAppEventBus(DisconnectedClientAppEventBus event) {
        ((InteractionUI) UI.getCurrent()).showNotification("", "Disconnected", Notification.Type.TRAY_NOTIFICATION);
    }
}
