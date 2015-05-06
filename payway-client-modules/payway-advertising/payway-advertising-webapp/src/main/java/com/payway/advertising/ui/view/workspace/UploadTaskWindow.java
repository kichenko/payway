/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import java.util.HashMap;
import java.util.Map;

/**
 * UploadInfoWindow
 *
 * @author Sergey Kichenko
 * @created 06.10.15 00:00
 */
public class UploadTaskWindow extends Window implements UploadTask.UploadListener {

    private final Table tableTasks = new Table();
    private final Map<Object, UploadTask> mapTask = new HashMap<>();

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
        UI.getCurrent().addWindow(this);
    }

    @Override
    public void close() {
        UI.getCurrent().removeWindow(this);
    }

    public void addUploadTask(final Upload upload) {

        Button btnCancel = new Button("Cancel");
        btnCancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (event.getButton().getData() != null) {
                    UploadTask task = (UploadTask) mapTask.get(event.getButton().getData());
                    if (task != null) {
                        task.getUpload().interruptUpload();
                    }
                }
            }
        });

        Object itemID = tableTasks.addItem(new Object[]{"", new ProgressBar(), "In progress...", btnCancel}, null);
        btnCancel.setData(itemID);

        mapTask.put(itemID, new UploadTask(upload, this, itemID));
    }

    @Override
    public void uploadStarted(UploadTask task, String fileName) {
        Item item = tableTasks.getItem(task.getTaskId());
        item.getItemProperty("FileName").setValue(fileName);
        item.getItemProperty("Status").setValue("Start");
        ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(0f);
    }

    @Override
    public void updateProgress(UploadTask task, final long readBytes, final long contentLength) {//
        Item item = tableTasks.getItem(task.getTaskId());
        ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(readBytes / (float) contentLength);
        item.getItemProperty("Status").setValue("In progress");
    }

    @Override
    public void uploadFailed(UploadTask task) {
        Item item = tableTasks.getItem(task.getTaskId());
        item.getItemProperty("Status").setValue("Failed");
        //mapTask.remove(task.getTaskId());
    }

    @Override
    public void uploadFinished(UploadTask task) {
        Item item = tableTasks.getItem(task.getTaskId());
        item.getItemProperty("Status").setValue("Finish");
        //mapTask.remove(task.getTaskId());
    }

    @Override
    public void uploadSucceeded(UploadTask task) {
        Item item = tableTasks.getItem(task.getTaskId());
        item.getItemProperty("Status").setValue("Succeess");
        //mapTask.remove(task.getTaskId());
    }
}
