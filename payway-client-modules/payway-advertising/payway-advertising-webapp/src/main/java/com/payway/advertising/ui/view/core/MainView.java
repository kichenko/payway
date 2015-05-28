/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.core.service.bean.BeanService;
import com.payway.advertising.ui.AbstractUI;
import com.payway.advertising.ui.bus.events.CloseNotificationsButtonPopupWindowsEvent;
import com.payway.advertising.ui.component.FileUploadPanel;
import com.payway.advertising.ui.component.NotificationsButton;
import com.payway.advertising.ui.component.ProgressBarWindow;
import com.payway.advertising.ui.component.SideBarMenu;
import com.payway.advertising.ui.component.UploadButtonWrapper;
import com.payway.advertising.ui.component.UploadTaskPanel;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalSplitPanel;
import java.util.Collection;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public static final float SIDEBAR_DEFAULT_WIDTH_PERCENT = 20;
    public static final float UPLOADS_DEFAULT_HEIGHT_PERCENT = 50;
    public static final float SIDEBAR_DEFAULT_HEIGHT_PERCENT = 50;

    @Autowired
    @Qualifier(value = "viewFactory")
    private ViewFactory viewFactory;

    @Autowired
    @Qualifier(value = "beanService")
    private BeanService beanService;

    @UiField
    private CssLayout mainViewLayout;

    @UiField
    private MenuBar userMenu;

    @UiField
    private MenuBar menuBar;

    @UiField
    private UploadButtonWrapper btnFileUploadToolBar;

    @UiField
    @Getter
    private NotificationsButton btnNotifications;

    @UiField
    @Getter
    private SideBarMenu sideBarMenu;

    @UiField
    private UploadTaskPanel uploadTaskPanel;

    @UiField
    private FileUploadPanel fileUploadPanel;

    @UiField
    private CssLayout panelContent;

    @UiField
    private HorizontalSplitPanel splitHorizontalPanel;

    @UiField
    private VerticalSplitPanel splitVerticalPanel;

    @UiField
    private VerticalSplitPanel splitVerticalPanelUploads;

    @UiField
    private CssLayout layoutUploads;

    @UiField
    private CssLayout layoutLeft;

    @UiField
    private CssLayout layoutRight;

    @UiField
    private CssLayout layoutSideBar;

    @UiField
    private CssLayout layoutUploadTasks;

    @UiField
    private CssLayout layoutFileUpload;

    private ProgressBarWindow progressBarWindow;

    private SlideBarMenuButtonClickCallback sbMenuButtonClickCallback;

    @PostConstruct
    void init() {
        setSizeFull();
        setCompositionRoot(Clara.create("MainView.xml", this));
        progressBarWindow = new ProgressBarWindow();

        splitHorizontalPanel.setFirstComponent(layoutLeft);
        splitHorizontalPanel.setSecondComponent(layoutRight);
        splitHorizontalPanel.setSplitPosition(SIDEBAR_DEFAULT_WIDTH_PERCENT, Unit.PERCENTAGE);

        splitVerticalPanel.setFirstComponent(layoutSideBar);
        splitVerticalPanel.setSecondComponent(layoutUploads);
        splitVerticalPanel.setSplitPosition(SIDEBAR_DEFAULT_HEIGHT_PERCENT, Unit.PERCENTAGE);

        splitVerticalPanelUploads.setFirstComponent(layoutUploadTasks);
        splitVerticalPanelUploads.setSecondComponent(layoutFileUpload);
        splitVerticalPanelUploads.setSplitPosition(UPLOADS_DEFAULT_HEIGHT_PERCENT, Unit.PERCENTAGE);

        mainViewLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                if (btnNotifications.isPopup()) {
                    AbstractUI ui = (AbstractUI) getUI();
                    if (ui != null) {
                        ui.getSessionEventBus().sendNotification(new CloseNotificationsButtonPopupWindowsEvent());
                    }
                }
            }
        });

        btnNotifications.setBeanService(beanService);
    }

    @Override
    public void initialize() {
        //
    }

    /**
     * Создать меню пользователя
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

        AbstractWorkspaceView v = (AbstractWorkspaceView) view;
        if (v != null) {
            v.setButtonFileUploadToolBar(btnFileUploadToolBar);
            v.setUploadTaskPanel(uploadTaskPanel);
            v.setProgressBarWindow(progressBarWindow);
            v.setFileUploadPanel(fileUploadPanel);
            v.setMenuBar(menuBar);
            v.activate();
        }
    }
}
