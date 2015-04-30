/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.core;

import com.payway.advertising.ui.component.SideBarMenu;
import com.payway.advertising.ui.view.sample.DashBoardSampleView;
import com.payway.model.messaging.auth.User;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import javax.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * Главное окно
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@Component
@NoArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public final class MainView extends CustomComponent implements SideBarMenu.SideBarMenuItemButton.SideBarMenuItemButtonClickListener {

    @UiField
    private MenuBar userMenu;

    @UiField
    private SideBarMenu sideBarMenu;

    @UiField
    private CssLayout panelContent;

    @PostConstruct
    void init() {
        setSizeFull();
        setCompositionRoot(Clara.create("MainView.xml", this));
    }

    /**
     * Создать меню пользователя
     */
    public void initializeUserMenu() {
        MenuBar.MenuItem settingsItem = userMenu.addItem(((User) VaadinSession.getCurrent().getAttribute(AdvertisingSessionAttributeType.USER.value())).username(), null);
        settingsItem.addItem("Sign Out", new MenuBar.Command() {
            @Override
            public void menuSelected(final MenuBar.MenuItem selectedItem) {
                VaadinSession.getCurrent().close();
                Page.getCurrent().reload();
            }
        });
    }

    /**
     * Создать меню в SideBar
     */
    public void initializeSideBarMenu() {
        sideBarMenu.addMenuItem("dashboard", "DashBoard", FontAwesome.HOME, this);
        sideBarMenu.addMenuItem("sales", "Sales", FontAwesome.BAR_CHART_O, this);
        sideBarMenu.addMenuItem("transactions", "Transactions", FontAwesome.TABLE, this);
        sideBarMenu.addMenuItem("reports", "Reports", FontAwesome.FILE_TEXT_O, this);
        sideBarMenu.addMenuItem("schedule", "Schedule", FontAwesome.CALENDAR_O, this);
    }

    /**
     * Обработчик клика по меню в SideBar
     *
     * @param button
     * @param event
     */
    @Override
    public void clickSideBarMenuItemButton(SideBarMenu.SideBarMenuItemButton button, Button.ClickEvent event) {
        panelContent.addComponent(new DashBoardSampleView());
    }
}
