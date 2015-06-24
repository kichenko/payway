/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.vaadin.server.ThemeResource;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.peter.imagescaler.ImageScaler;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePreviewEmpty
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
public final class FilePreviewEmpty extends AbstractFilePreview {

    private static final long serialVersionUID = 2617768620709657298L;

    @UiField
    private ImageScaler imageScaler;

    public FilePreviewEmpty() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewEmpty.xml", this));
        imageScaler.setImage(new ThemeResource("images/components/file_preview/file_preview_empty.png"), 100, 100);
    }
}
