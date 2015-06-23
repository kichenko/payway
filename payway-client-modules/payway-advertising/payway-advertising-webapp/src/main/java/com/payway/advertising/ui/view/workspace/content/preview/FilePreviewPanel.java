/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.payway.advertising.model.DbFileType;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FilePreviewPanel
 *
 * @author Sergey Kichenko
 * @created 23.06.15 00:00
 */
@Slf4j
public class FilePreviewPanel extends Panel {

    private static final long serialVersionUID = 7348299745816567433L;

    @UiField
    private VerticalLayout layoutFilePreview;

    public FilePreviewPanel() {
        init();
    }

    private void init() {
        setCaption("File preview");
        setIcon(new ThemeResource("images/file_preview_panel.png"));
        setContent(Clara.create("FilePreviewPanel.xml", this));
    }

    public void show(String fileName, String filePath, DbFileType kind) {

        layoutFilePreview.removeAllComponents();

        if (kind == null) {
            layoutFilePreview.addComponent(new FilePreviewEmpty());
            return;
        }

        switch (kind) {

            case Archive:
                layoutFilePreview.addComponent(new FilePreviewArchive());
                break;

            case Banner:
            case Logo:
                layoutFilePreview.addComponent(new FilePreviewImage());
                break;

            case Clip:
            case Popup:
                layoutFilePreview.addComponent(new FilePreviewVideo());
                break;

            case Unknown:
                layoutFilePreview.addComponent(new FilePreviewEmpty());
                break;

            default:
                layoutFilePreview.addComponent(new FilePreviewEmpty());
        }
    }

    public void clear() {
        layoutFilePreview.removeAllComponents();
        layoutFilePreview.addComponent(new FilePreviewEmpty());
    }
}
