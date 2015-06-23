/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;

/**
 * FilePreviewArchive
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
public final class FilePreviewArchive extends AbstractFilePreview {
    
    private static final long serialVersionUID = 6894765612390555165L;

    public FilePreviewArchive() {
        init();
    }

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewArchive.xml", this));
    }
}
