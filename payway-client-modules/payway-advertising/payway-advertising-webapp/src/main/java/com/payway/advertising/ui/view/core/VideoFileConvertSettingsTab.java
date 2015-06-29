/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.ui.view.core.container.FormatContainerBeanContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Slider;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * VideoFileConvertSettingsTab
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public class VideoFileConvertSettingsTab extends AbstractAdvertisingSettingsTab {

    private static final long serialVersionUID = 3914931494662309458L;

    @UiField
    private CheckBox chConvertVideo;

    @UiField
    private ComboBox cbFormatContainer;

    @UiField
    private Slider sliderVideoBitRate;

    public VideoFileConvertSettingsTab(SettingsAppService settingsAppService) {
        super(settingsAppService);
        init();
    }

    private void init() {

        addComponent(Clara.create("VideoFileConvertSettingsTab.xml", this));

        sliderVideoBitRate.setMin(0);
        sliderVideoBitRate.setMax(5000);
        sliderVideoBitRate.setValue(1500.0);
        chConvertVideo.setValue(Boolean.FALSE);
        cbFormatContainer.setContainerDataSource(new FormatContainerBeanContainer());

        //fucked!
        //((FormatContainerBeanContainer)cbFormatContainer.getContainerDataSource()).addAll(settingsAppService.getSupportedFormatContainers());
    }

    @Override
    public void load() {
        //
    }

    @Override
    public void save() {
        //
    }

    @Override
    public void cancel() {
        //
    }
}
