/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.List;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * AdvertisingSettingsWindow
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public class AdvertisingSettingsWindow extends Window {

    private static final long serialVersionUID = 9007662088970331759L;

    private List<AbstractAdvertisingSettingsTab> tabs;

    @UiField
    private TabSheet tabSheetSettings;

    public AdvertisingSettingsWindow(String caption, SettingsAppService settingsAppService) {

        init(settingsAppService);
        setCaption(caption);
    }

    private void init(SettingsAppService settingsAppService) {
        setContent(Clara.create("AdvertisingSettingsWindow.xml", this));
        tabs.add(new VideoFileConvertSettingsTab(settingsAppService));
        tabSheetSettings.addTab(tabs.get(0), "Video", new ThemeResource("images/tab_app_settings_video.png"));
    }

    @UiHandler(value = "btnCancel")
    public void onClickCancel(Button.ClickEvent event) {

        for (AbstractAdvertisingSettingsTab tab : tabs) {
            tab.cancel();
        }
        close();
    }

    @UiHandler(value = "btnSave")
    public void onClickSave(Button.ClickEvent event) {

        for (AbstractAdvertisingSettingsTab tab : tabs) {
            tab.save();
        }
    }

    public void show() {
        if (!isAttached()) {
            UI.getCurrent().addWindow(this);
        }
    }
}
