/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.payway.advertising.ui.upload.UploadListener;
import com.payway.advertising.ui.upload.UploadTask;
import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import lombok.extern.slf4j.Slf4j;

/**
 * UploadTaskPanel
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
@Slf4j
public class UploadTaskPanel extends Panel implements UploadListener {

    private static final long serialVersionUID = 7412888638210524568L;

    private final Table gridTasks = new Table();

    public UploadTaskPanel() {
        setIcon(new ThemeResource("images/components/upload_task_panel/upload_task_panel.png"));
        setContent(initGridTask());
    }

    private Table initGridTask() {

        gridTasks.setSizeFull();
        gridTasks.addContainerProperty("File name", String.class, null);
        gridTasks.addContainerProperty("Progress", ProgressBar.class, null);
        gridTasks.addContainerProperty("Status", String.class, null);
        gridTasks.addContainerProperty("Cancel/Drop", Button.class, null);
        gridTasks.addStyleName("upload-task-panel-grid-task");

        return gridTasks;
    }

    public void addUploadTask(final UploadTask uploadTask) {

        uploadTask.addListener(this);

        Button btnCancel = new Button(new ThemeResource("images/components/upload_task_panel/btn_delete_cancel_upload.png"));
        btnCancel.setStyleName("common-no-space-image-button");

        btnCancel.setData(uploadTask);
        btnCancel.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

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
    public void updateProgress(final UploadTask task, final long readBytes, final long contentLength) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                Item item = gridTasks.getItem(task.getTaskId());
                if (item != null) {
                    ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(readBytes / (float) contentLength);
                    item.getItemProperty("Status").setValue("In progress");
                }
            }
        });
    }

    @Override
    public void uploadFailed(final UploadTask task, final boolean isInterrupted) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                Item item = gridTasks.getItem(task.getTaskId());
                if (item != null) {
                    if (isInterrupted) {
                        item.getItemProperty("Status").setValue("Cancel");
                    } else {
                        item.getItemProperty("Status").setValue("Failed");
                    }
                }
            }
        });
    }

    @Override
    public void uploadSucceeded(final UploadTask task) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                Item item = gridTasks.getItem(task.getTaskId());
                if (item != null) {
                    item.getItemProperty("Status").setValue("Succeess");
                }
            }
        });
    }
}
