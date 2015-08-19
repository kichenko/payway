/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content.preview;

import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.model.DbFileType;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.io.InputStream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    private static final String FILE_PREVIEW_DEFAULT_RESOURCE_NAME = "images/components/file_preview/file_preview_empty.png";

    @UiField
    private VerticalLayout layoutFilePreview;

    @Getter
    @Setter
    private FileSystemManagerService fileSystemManagerService;

    public FilePreviewPanel() {
        init();
    }

    private void init() {
        setSizeFull();
        setCaption("File preview");
        setIcon(new ThemeResource("images/file_preview_panel.png"));
        setContent(Clara.create("FilePreviewPanel.xml", this));
    }

    private InputStream getFileSystemContentStream(final String path) {
        try {
            return fileSystemManagerService.getInputStream(new FileSystemObject(path, FileSystemObject.FileType.FILE, 0L, null));
        } catch (Exception ex) {
            log.error("Can not get file input stream - ", ex);
        }

        return null;
    }

    private InputStream getThemeContentStream(final String resourceName) {

        InputStream is = null;
        try {
            is = VaadinService.getCurrent().getThemeResourceAsStream(UI.getCurrent(), UI.getCurrent().getTheme(), resourceName);
        } catch (Exception ex) {
            log.error("Can not get input stream from theme resource - {}", ex);
        }

        return is;
    }

    public void show(String fileName, String filePath, DbFileType kind) {

        layoutFilePreview.removeAllComponents();

        if (kind == null) {
            layoutFilePreview.addComponent(new FilePreviewEmpty().build(getThemeContentStream(FILE_PREVIEW_DEFAULT_RESOURCE_NAME), StringUtils.substringAfterLast(FILE_PREVIEW_DEFAULT_RESOURCE_NAME, "/")));
            return;
        }

        switch (kind) {

            case Archive:
                layoutFilePreview.addComponent(new FilePreviewArchive().build(getFileSystemContentStream(filePath), fileName));
                break;

            case Banner:
            case Logo:
                layoutFilePreview.addComponent(new FilePreviewImage().build(getFileSystemContentStream(filePath), fileName));
                break;

            case Clip:
            case Popup:
                layoutFilePreview.addComponent(new FilePreviewVideo().build(getFileSystemContentStream(filePath), fileName));
                break;

            case Unknown:
                layoutFilePreview.addComponent(new FilePreviewEmpty().build(getThemeContentStream(FILE_PREVIEW_DEFAULT_RESOURCE_NAME), StringUtils.substringAfterLast(FILE_PREVIEW_DEFAULT_RESOURCE_NAME, "/")));
                break;

            default:
                layoutFilePreview.addComponent(new FilePreviewEmpty().build(getThemeContentStream(FILE_PREVIEW_DEFAULT_RESOURCE_NAME), StringUtils.substringAfterLast(FILE_PREVIEW_DEFAULT_RESOURCE_NAME, "/")));
        }
    }

    public void clear() {
        layoutFilePreview.removeAllComponents();
        layoutFilePreview.addComponent(new FilePreviewEmpty().build(getThemeContentStream(FILE_PREVIEW_DEFAULT_RESOURCE_NAME), StringUtils.substringAfterLast(FILE_PREVIEW_DEFAULT_RESOURCE_NAME, "/")));
    }
}
