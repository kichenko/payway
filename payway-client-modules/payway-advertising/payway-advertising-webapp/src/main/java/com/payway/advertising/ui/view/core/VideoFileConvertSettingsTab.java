/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.core.service.app.settings.SettingsAppService;
import com.payway.advertising.ui.view.core.container.FormatContainerBeanContainer;
import com.payway.media.core.container.FormatContainer;
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

    public VideoFileConvertSettingsTab(String caption, SettingsAppService settingsAppService) {
        super(caption, settingsAppService);
        init();
    }

    private void init() {

        addComponent(Clara.create("VideoFileConvertSettingsTab.xml", this));

        sliderVideoBitRate.setMin(0);
        sliderVideoBitRate.setMax(10000);
        sliderVideoBitRate.setValue(1500.0);
        chConvertVideo.setValue(Boolean.FALSE);
        cbFormatContainer.setContainerDataSource(new FormatContainerBeanContainer());
    }

    @Override
    public void load() {
        ((FormatContainerBeanContainer) cbFormatContainer.getContainerDataSource()).addAll(settingsAppService.getSupportedFormatContainers());
        cbFormatContainer.select(settingsAppService.getCurrentFormatContainer().getId());
        chConvertVideo.setValue(settingsAppService.isConvertVideoFiles());
        sliderVideoBitRate.setValue(settingsAppService.getVideoAttributes() == null ? 0 : (double) settingsAppService.getVideoAttributes().getBitRate() / 1000);
    }

    @Override
    public void save() throws Exception {

        //get old settings
        FormatContainer format = settingsAppService.getCurrentFormatContainer();
        int videoBitrate = settingsAppService.getVideoAttributes() == null ? 0 : settingsAppService.getVideoAttributes().getBitRate();
        boolean isConvertVideoFiles = settingsAppService.isConvertVideoFiles();

        try {
            //save
            if (settingsAppService.getVideoAttributes() != null) {
                settingsAppService.getVideoAttributes().setBitRate(sliderVideoBitRate.getValue().intValue() * 1000);
            }

            settingsAppService.setConvertVideoFiles(chConvertVideo.getValue());
            for (FormatContainer fmt : settingsAppService.getSupportedFormatContainers()) {
                if (fmt.getId().equals((String) cbFormatContainer.getValue())) {
                    settingsAppService.setCurrentFormatContainer(fmt);
                    break;
                }
            }

            settingsAppService.save();
        } catch (Exception ex) {

            //revert
            settingsAppService.setConvertVideoFiles(isConvertVideoFiles);
            settingsAppService.setCurrentFormatContainer(format);
            if (settingsAppService.getVideoAttributes() != null) {
                settingsAppService.getVideoAttributes().setBitRate(videoBitrate);
            }

            throw ex;
        }
    }

    @Override
    public void cancel() {
        //
    }
}
