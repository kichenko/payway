/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.commons.webapp.ui.InteractionUI;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

/**
 * AdvertisingSettingsWindow
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Slf4j
public class AdvertisingSettingsWindow extends Window {

    private static final long serialVersionUID = 9007662088970331759L;

    private final List<AbstractAdvertisingSettingsTab> tabs = new ArrayList<>(1);

    @UiField
    private TabSheet tabSheetSettings;

    public AdvertisingSettingsWindow(String caption, SettingsAppService settingsAppService) {

        setModal(true);
        setResizable(false);
        setCaption(caption);
        setWidth(570, Unit.PIXELS);
        setHeight(305, Unit.PIXELS);
        init(settingsAppService);
    }

    private void init(SettingsAppService settingsAppService) {
        setContent(Clara.create("AdvertisingSettingsWindow.xml", this));
        tabs.add(new VideoFileConvertSettingsTab("Video", settingsAppService));
        tabSheetSettings.addTab(tabs.get(0), tabs.get(0).getCaption(), new ThemeResource("images/tab_app_settings_video.png"));
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
            try {
                tab.save();
            } catch (Exception ex) {
                log.error("Could not save setting of tab [{}] - {}", tab.getCaption(), ex);
                ((InteractionUI) getUI()).showNotification("Save settings", String.format("Could not save settings of tab %s", tab.getCaption()), Notification.Type.ERROR_MESSAGE);
            }
        }

        close();
    }

    public void show() {
        
        if (!isAttached()) {
            for (AbstractAdvertisingSettingsTab tab : tabs) {
                tab.load();
            }
            UI.getCurrent().addWindow(this);
        }
    }
}
