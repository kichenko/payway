/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

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

    public interface FileUploadWindowAction {

        void onOk(Upload upload);

        void onCancel();
    }

    @UiField
    private Upload upload;

    private FileUploadWindowAction listener;

    public FileUploadWindow(Upload.Receiver receiver) {
        setModal(true);
        setClosable(true);
        setDraggable(false);
        setResizable(false);
        setContent(Clara.create("FileUploadWindow.xml", this));
        
        upload.setImmediate(false);
        upload.setReceiver(receiver);
        upload.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent event) {
                if (listener != null) {
                    UI.getCurrent().removeWindow(FileUploadWindow.this);
                    listener.onOk(FileUploadWindow.this.upload);
                }
            }
        });
    }

    public Upload getUpload() {
        return upload;
    }

    public void show() {
        UI.getCurrent().addWindow(this);
    }

    @Override
    public void close() {
        UI.getCurrent().removeWindow(FileUploadWindow.this);
        if (listener != null) {
            listener.onCancel();
        }
    }

    public void addUploadActionListener(FileUploadWindowAction listener) {
        this.listener = listener;
    }
}
