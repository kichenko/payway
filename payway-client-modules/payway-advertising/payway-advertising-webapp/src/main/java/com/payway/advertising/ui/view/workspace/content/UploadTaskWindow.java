/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * UploadInfoWindow
 *
 * @author Sergey Kichenko
 * @created 06.10.15 00:00
 */
public class UploadTaskWindow extends Window implements UploadListener {

    private final Table tableTasks = new Table();

    public UploadTaskWindow() {

        setWidth(500, Unit.PIXELS);
        setHeight(400, Unit.PIXELS);

        tableTasks.setSizeFull();
        tableTasks.addContainerProperty("FileName", String.class, null);
        tableTasks.addContainerProperty("Progress", ProgressBar.class, null);
        tableTasks.addContainerProperty("Status", String.class, null);
        tableTasks.addContainerProperty("Cancel", Button.class, null);

        setContent(tableTasks);

        setClosable(true);
        setResizable(true);
        setCaption("Upload task window");
    }

    public void show() {
        if (this.getParent() == null) {
            UI.getCurrent().addWindow(this);
        }
    }

    @Override
    public void close() {
        UI.getCurrent().removeWindow(this);
    }

    public void addUploadTask(final UploadTask uploadTask) {

        uploadTask.setListener(this);

        Button btnCancel = new Button("Cancel");

        btnCancel.setData(uploadTask);
        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (event.getButton().getData() != null) {
                    UploadTask task = (UploadTask) event.getButton().getData();
                    if (task != null) {
                        task.interrupt();
                        event.getButton().setEnabled(false);
                    }
                }
            }
        });

        tableTasks.addItem(new Object[]{uploadTask.getFileName(), new ProgressBar(), "Start", btnCancel}, uploadTask.getTaskId());
    }

    @Override
    public void updateProgress(UploadTask task, final long readBytes, final long contentLength) {
        Item item = tableTasks.getItem(task.getTaskId());
        if (item != null) {
            ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(readBytes / (float) contentLength);
            item.getItemProperty("Status").setValue("In progress");
        }
    }

    @Override
    public void uploadFailed(UploadTask task, boolean isInterrupted) {
        Item item = tableTasks.getItem(task.getTaskId());
        if (item != null) {
            if (isInterrupted) {
                item.getItemProperty("Status").setValue("Cancel");
            } else {
                item.getItemProperty("Status").setValue("Failed");
            }
            ((Button) item.getItemProperty("Cancel").getValue()).setEnabled(false);
        }
    }

    @Override
    public void uploadSucceeded(UploadTask task) {
        Item item = tableTasks.getItem(task.getTaskId());
        if (item != null) {
            item.getItemProperty("Status").setValue("Succeess");
            ((Button) item.getItemProperty("Cancel").getValue()).setEnabled(false);
        }
    }
}
