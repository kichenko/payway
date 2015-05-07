/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.teemu.clara.Clara;
import org.vaadin.teemu.clara.binder.annotation.UiField;

/**
 * FileUploadWindow
 *
 * @author Sergey Kichenko
 * @created 06.05.15 00:00
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileUploadWindow extends Window {

    public interface FileUploadWindowEvent {

        boolean onOk(UploadTaskFileInput uploadTask);

        void onCancel();
    }

    @UiField
    private Upload upload;

    private FileUploadWindowEvent listener;
    private UploadTaskFileInput uploadTask;

    public FileUploadWindow(String caption, UploadTaskFileInput task, FileUploadWindowEvent event) {

        initUI(caption);

        uploadTask = task;
        listener = event;
        uploadTask.setUploadObject(upload);
        upload.setReceiver(uploadTask);

        upload.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent event) {
                uploadTask.setFileName(event.getFilename());
                if (listener != null) {
                    if (listener.onOk(uploadTask)) {
                        UI.getCurrent().removeWindow(FileUploadWindow.this);
                    }
                } else {
                    UI.getCurrent().removeWindow(FileUploadWindow.this);
                }
            }
        });
    }

    private void initUI(String caption) {
        setModal(true);
        setClosable(true);
        setDraggable(false);
        setResizable(false);
        setCaption(caption);
        setContent(Clara.create("FileUploadWindow.xml", this));
    }

    public void show() {
        if (this.getParent() == null) {
            UI.getCurrent().addWindow(this);
        }
    }

    @Override
    public void close() {
        UI.getCurrent().removeWindow(FileUploadWindow.this);
        if (listener != null) {
            listener.onCancel();
        }
    }
}
