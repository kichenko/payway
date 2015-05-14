/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.ui.component.SideBarMenu;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import java.util.Collection;
import javax.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * Главное окно
 *
 * @author Sergey Kichenko
 * @created 20.04.15 00:00
 */
@UIScope
@Component
@NoArgsConstructor
public class MainView extends CustomComponent implements CustomComponentInitialize, SideBarMenu.SideBarMenuButton.SideBarMenuButtonClickListener {

    public interface SlideBarMenuButtonClickCallback {

        void onClick(SideBarMenu.SideBarMenuButton button, Button.ClickEvent event);
    }

    @Autowired
    private ViewFactory viewFactory;

    @UiField
    private MenuBar userMenu;

    @UiField
    private SideBarMenu sideBarMenu;

    @UiField
    private CssLayout panelContent;

    private SlideBarMenuButtonClickCallback sbMenuButtonClickCallback;

    @PostConstruct
    void init() {
        setSizeFull();
        setCompositionRoot(Clara.create("MainView.xml", this));
    }

    @Override
    public void initialize() {
        //
    }

    /**
     * Создать меню пользователя
     *
     * @param caption
     * @param items
     */
    public void initializeUserMenu(String caption, Collection<ImmutablePair<String, MenuBar.Command>> items) {
        MenuBar.MenuItem settingsItem = userMenu.addItem(caption, null);
        for (Pair<String, MenuBar.Command> i : items) {
            settingsItem.addItem(i.getLeft(), i.getRight());
        }
    }

    /**
     * Создать меню в SideBar
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
     * Обработчик клика по меню в SideBar
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

        if (view instanceof AbstractView) {
            ((AbstractView) view).activate();
        }
    }
}
