/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.component;

import com.payway.advertising.core.handlers.FileHandlerArgs;
import com.payway.advertising.core.handlers.FileUploadedProcessorTask;
import com.payway.advertising.core.service.bean.BeanService;
import com.payway.advertising.ui.upload.AbstractFileUploadedProcessorTaskListener;
import com.payway.advertising.ui.upload.UploadListener;
import com.payway.advertising.ui.upload.UploadTask;
import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;

/**
 * UploadTaskPanel
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
@Slf4j
public class UploadTaskPanel extends Panel implements UploadListener {

    private static final long serialVersionUID = 7412888638210524568L;

    public interface UploadEventListener {

        void onFinish(UploadTask task, FileHandlerArgs args);
    }

    public enum UploadStatusType {

        Start("Start"),
        Upload("Upload"),
        Process("Process"),
        Cancel("Cancel"),
        Fail("Failed"),
        Finish("Finish");

        @Getter
        @Setter(AccessLevel.PRIVATE)
        private String status;

        private UploadStatusType(String st) {
            setStatus(st);
        }
    }

    private final Table gridTasks = new Table();

    @Getter
    @Setter
    private BeanService beanService;

    @Getter
    @Setter
    private UploadEventListener uploadEventListener;

    public UploadTaskPanel() {
        setIcon(new ThemeResource("images/components/upload_task_panel/upload_task_panel.png"));
        setContent(initGridTask());
    }

    private Table initGridTask() {

        gridTasks.setSizeFull();
        gridTasks.addContainerProperty("File name", String.class, null);
        gridTasks.addContainerProperty("Status", String.class, null);
        gridTasks.addContainerProperty("Progress", ProgressBar.class, null);
        gridTasks.addContainerProperty("Decription", String.class, null);
        gridTasks.addContainerProperty("Cancel", Button.class, null);
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

        gridTasks.addItem(new Object[]{uploadTask.getFileName(), UploadStatusType.Start.getStatus(), new ProgressBar(), "Start upload", btnCancel}, uploadTask.getTaskId());
    }

    @Override
    public void updateProgress(final UploadTask task, final long readBytes, final long contentLength) {
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                Item item = gridTasks.getItem(task.getTaskId());
                if (item != null) {
                    item.getItemProperty("Decription").setValue("Uploading...");
                    item.getItemProperty("Status").setValue(UploadStatusType.Upload.getStatus());
                    ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(readBytes / (float) contentLength);
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
                        item.getItemProperty("Decription").setValue("Cancel");
                        item.getItemProperty("Status").setValue(UploadStatusType.Cancel.getStatus());
                    } else {
                        item.getItemProperty("Decription").setValue("Fail");
                        item.getItemProperty("Status").setValue(UploadStatusType.Fail.getStatus());
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
                try {

                    if (item == null) {
                        throw new Exception("Empty item in table row");
                    }

                    Button button = (Button) item.getItemProperty("Cancel").getValue();
                    if (button == null) {
                        throw new Exception("Empty 'cancel' button in table row");
                    }

                    UploadTask uploadTask = (UploadTask) button.getData();
                    if (uploadTask == null) {
                        throw new Exception("Empty upload task in data of 'cancel' button control in table row");
                    }

                    item.getItemProperty("Decription").setValue("Process");
                    item.getItemProperty("Status").setValue(UploadStatusType.Process.getStatus());
                    ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(0.0F);

                    uploadTask.setFileUploadedProcessorTaskListener(new AbstractFileUploadedProcessorTaskListener(uploadTask, getUI()) {

                        @Override
                        public void onStart(int countTask) {
                            this.access(new Runnable() {
                                @Override
                                public void run() {
                                    Item item = gridTasks.getItem(getUploadTaskRef().getTaskId());
                                    if (item == null) {
                                        log.error("Empty item grid task then start processing files");
                                        return;
                                    }

                                    item.getItemProperty("Decription").setValue("Process");
                                    item.getItemProperty("Status").setValue(UploadStatusType.Process.getStatus());
                                    gridTasks.refreshRowCache();
                                    getUIRef().push();
                                }
                            });
                        }

                        @Override
                        public void onProcess(final int currentTask, final int countTask) {
                            this.access(new Runnable() {
                                @Override
                                public void run() {
                                    Item item = gridTasks.getItem(getUploadTaskRef().getTaskId());
                                    if (item == null) {
                                        log.error("Empty item grid task then process processing files");
                                        return;
                                    }

                                    item.getItemProperty("Decription").setValue(String.format("Processing task %d/%d", currentTask, countTask));
                                    ((ProgressBar) item.getItemProperty("Progress").getValue()).setValue(currentTask / (float) countTask);
                                    item.getItemProperty("Status").setValue(UploadStatusType.Process.getStatus());
                                    gridTasks.refreshRowCache();
                                    getUIRef().push();
                                }
                            });
                        }

                        @Override
                        public void onFail(final int currentTask, final int countTask) {
                            this.access(new Runnable() {
                                @Override
                                public void run() {
                                    Item item = gridTasks.getItem(getUploadTaskRef().getTaskId());
                                    if (item == null) {
                                        log.error("Empty item grid task then fail processing files");
                                        return;
                                    }

                                    item.getItemProperty("Decription").setValue(String.format("Failed on execute task %d/%d", currentTask, countTask));
                                    item.getItemProperty("Status").setValue(UploadStatusType.Fail.getStatus());
                                    gridTasks.refreshRowCache();
                                    getUIRef().push();
                                }
                            });
                        }

                        @Override
                        public void onFinish(final FileHandlerArgs args) {
                            this.access(new Runnable() {
                                @Override
                                public void run() {
                                    Item item = gridTasks.getItem(getUploadTaskRef().getTaskId());
                                    if (item == null) {
                                        log.error("Empty item grid task then finish processing files");
                                        return;
                                    }

                                    item.getItemProperty("Decription").setValue("Finish");
                                    item.getItemProperty("Status").setValue(UploadStatusType.Finish.getStatus());
                                    gridTasks.refreshRowCache();
                                    getUIRef().push();

                                    //fire finish upload event
                                    if (uploadEventListener != null) {
                                        uploadEventListener.onFinish(getUploadTaskRef(), args);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onInterrupt() {
                            this.access(new Runnable() {
                                @Override
                                public void run() {
                                    Item item = gridTasks.getItem(getUploadTaskRef().getTaskId());
                                    if (item == null) {
                                        log.error("Empty item grid task then interrput processing files");
                                        return;
                                    }

                                    item.getItemProperty("Decription").setValue("Cancel");
                                    item.getItemProperty("Status").setValue(UploadStatusType.Cancel.getStatus());
                                    gridTasks.refreshRowCache();
                                    getUIRef().push();
                                }
                            });
                        }
                    });

                    TaskExecutor taskExecutor = (TaskExecutor) beanService.getBean("app.advertising.ServerTaskExecutor");
                    if (taskExecutor == null) {
                        throw new Exception("Could not get task executor");
                    }

                    taskExecutor.execute((FileUploadedProcessorTask) beanService.getBean("app.advertising.FileUploadedProcessorTask",
                            uploadTask.getUploadPath(),
                            uploadTask.getUploadTempFileName(),
                            uploadTask.getDestFilePath(),
                            uploadTask.getFileName(),
                            uploadTask.getFileUploadedProcessorTaskListener())
                    );

                } catch (Exception ex) {
                    log.error("Could not run file upload processor task", ex);
                    if (item != null) {
                        item.getItemProperty("Decription").setValue("Fail");
                        item.getItemProperty("Status").setValue(UploadStatusType.Fail.getStatus());
                    }
                }
            }
        });
    }
}
