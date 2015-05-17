/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.payway.advertising.ui.view.workspace.content.UploadListener;
import com.payway.advertising.ui.view.workspace.content.UploadTask;
import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import lombok.extern.slf4j.Slf4j;

/**
 * UploadTaskPanel
 *
 * @author Sergey Kichenko
 * @created 17.10.15 00:00
 */
@Slf4j
public class UploadTaskPanel extends Panel implements UploadListener {

    private final Table gridTasks = new Table();

    public UploadTaskPanel() {
        setIcon(new ThemeResource("images/components/upload-task-panel/upload-task-panel.png"));
        gridTasks.addContainerProperty("File name", String.class, null);
        gridTasks.addContainerProperty("Progress", ProgressBar.class, null);
        gridTasks.addContainerProperty("Status", String.class, null);
        gridTasks.addContainerProperty("Cancel/Drop", Button.class, null);
        gridTasks.setSizeFull();
        gridTasks.addStyleName("upload-task-panel-grid-task");
        setContent(gridTasks);
    }

    public void addUploadTask(final UploadTask uploadTask) {

        uploadTask.addListener(this);

        Button btnCancel = new Button(new ThemeResource("images/components/upload-task-panel/btn_delete_cancel_upload.png"));
        btnCancel.setStyleName("common-no-space-image-button");

        btnCancel.setData(uploadTask);
        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (event.getButton().getData() != null) {
                    UploadTask task = (UploadTask) event.getButton().getData();
                    if (task != null) {
                        task.interrupt();
                        gridTasks.removeItem(task.getTaskId());
                    }
                }
            }
        });

        gridTasks.addItem(new Object[]{uploadTask.getFileName(), new ProgressBar(), "Start", btnCancel}, uploadTask.getTaskId());
    }

    @Override
    public void updateProgress(UploadTask task, final long readBytes, final long contentLength) {
        Item item = gridTasks.getItem(task.getTaskId());
        if (item != null) {
            ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(readBytes / (float) contentLength);
            item.getItemProperty("Status").setValue("In progress");
        }
    }

    @Override
    public void uploadFailed(UploadTask task, boolean isInterrupted) {
        Item item = gridTasks.getItem(task.getTaskId());
        if (item != null) {
            if (isInterrupted) {
                item.getItemProperty("Status").setValue("Cancel");
            } else {
                item.getItemProperty("Status").setValue("Failed");
            }
        }
    }

    @Override
    public void uploadSucceeded(UploadTask task) {
        Item item = gridTasks.getItem(task.getTaskId());
        if (item != null) {
            item.getItemProperty("Status").setValue("Succeess");
        }
    }
}
