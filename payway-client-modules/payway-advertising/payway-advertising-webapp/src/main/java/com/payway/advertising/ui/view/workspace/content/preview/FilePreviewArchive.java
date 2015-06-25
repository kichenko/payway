/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import java.io.InputStream;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;

/**
 * FilePreviewArchive
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
@NoArgsConstructor
public class FilePreviewArchive extends AbstractFilePreview {

    private static final long serialVersionUID = 6894765612390555165L;

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewArchive.xml", this));
    }

    @Override
    protected void loadContent(InputStream stream, String fileName) {
        //
    }

    @Override
    public AbstractFilePreview build(InputStream stream, String fileName) {
        return this;
    }
}
