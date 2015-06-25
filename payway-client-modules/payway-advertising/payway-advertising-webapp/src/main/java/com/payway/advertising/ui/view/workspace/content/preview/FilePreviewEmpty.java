/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;

/**
 * FilePreviewEmpty
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
public class FilePreviewEmpty extends FilePreviewImage {

    private static final long serialVersionUID = 2617768620709657298L;

    @Override
    protected void init() {
        setSizeFull();
        addComponent(Clara.create("FilePreviewEmpty.xml", this));
    }
}
