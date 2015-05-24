/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.vaadin.event.dd.DropHandler;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FileUploadPanel
 *
 * @author Sergey Kichenko
 * @created 24.04.15 00:00
 */
public class FileUploadPanel extends Panel {

    @UiField
    private Button btnFileUpload;

    @UiField
    private VerticalLayout layoutDragDropPanel;

    @UiField
    private CssLayout layoutFileUpload;

    private DragAndDropWrapper wrapper;

    public FileUploadPanel() {
        init();
    }

    private void init() {
        setSizeFull();
        setContent(Clara.create("FileUploadPanel.xml", this));

        setIcon(new ThemeResource("images/components/file_upload_panel/file_upload_panel.png"));

        btnFileUpload.setIcon(new ThemeResource("images/components/file_upload_panel/file_attach.png"));
        btnFileUpload.addStyleName("common-no-space-image-button");

        wrapper = new DragAndDropWrapper(layoutDragDropPanel);
        wrapper.setSizeFull();
        layoutFileUpload.addComponent(wrapper);
    }

    public void addButtonUploadClickListener(Button.ClickListener clickListener) {
        btnFileUpload.addClickListener(clickListener);
    }

    public void addDragAndDropHandler(DropHandler handler) {
        wrapper.setDropHandler(handler);
    }
}
