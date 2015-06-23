/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.vaadin.ui.Video;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePreviewVideo
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
public final class FilePreviewVideo extends AbstractFilePreview {

    private static final long serialVersionUID = 8484774321723519679L;

    @UiField
    private Video video;

    public FilePreviewVideo() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewVideo.xml", this));
    }
}
