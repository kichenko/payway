/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.vaadin.server.Resource;
import com.vaadin.ui.Image;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePreviewImage
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
public final class FilePreviewImage extends AbstractFilePreview {

    private static final long serialVersionUID = 4340697649028702403L;

    @UiField
    private Image image;

    public FilePreviewImage() {
        init();
    }

    public FilePreviewImage(Resource resource) {
        init();
        image.setSource(resource);
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewImage.xml", this));
    }
}
