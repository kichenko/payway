/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.server.communication.FileUploadHandler;
import com.vaadin.ui.Upload;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * UploadTaskFileInput
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
public class UploadTaskFileInput implements UploadTask, Upload.Receiver, Upload.ProgressListener, Upload.FailedListener, Upload.SucceededListener {

    private UUID taskId;
    private Upload upload;
    private String fileName;
    private UploadListener listener;

    public UploadTaskFileInput() {
        taskId = UUID.randomUUID();
    }

    @Override
    public void setUploadObject(Object uploadObject) {
        this.upload = (Upload) uploadObject;        
        if (this.upload != null) {
            this.upload.addProgressListener(this);
            this.upload.addFailedListener(this);
            this.upload.addSucceededListener(this);
        }
    }

    @Override
    public Object getUploadObject() {
        return upload;
    }

    @Override
    public void interrupt() {
        if (upload != null) {
            upload.interruptUpload();
        }
    }

    @Override
    public UUID getTaskId() {
        return taskId;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setListener(UploadListener listener) {
        this.listener = listener;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
        if (listener != null) {
            listener.updateProgress(this, readBytes, contentLength);
        }
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        if (listener != null) {
            boolean isInterrupted = false;
            if (event.getReason() != null) {
                if (event.getReason().getClass().getName().equals(FileUploadHandler.UploadInterruptedException.class.getName())) {
                    isInterrupted = true;
                }
            }
            listener.uploadFailed(this, isInterrupted);
        }
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        if (listener != null) {
            listener.uploadSucceeded(this);
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                //
            }
        };
    }
}
