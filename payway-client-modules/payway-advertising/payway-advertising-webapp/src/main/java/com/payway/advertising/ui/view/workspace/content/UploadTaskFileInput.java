/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.server.communication.FileUploadHandler;
import com.vaadin.ui.Upload;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * UploadTaskFileInput
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
@Slf4j
public class UploadTaskFileInput extends AbstractUploadTask implements Upload.Receiver, Upload.ProgressListener, Upload.FailedListener, Upload.SucceededListener {

    private Upload upload;

    public UploadTaskFileInput() {
        super();
    }

    public UploadTaskFileInput(String path, int bufSize) {
        super(path, bufSize);
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
            isInterrupted = true;
            upload.interruptUpload();
        }
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
        if (listeners != null) {
            for (UploadListener l : listeners) {
                l.updateProgress(this, readBytes, contentLength);
            }
        }
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        //then upload is success, rename tmp uploaded file to original
        uploadFinished();
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        //then upload is failed, remove tmp uploaded file
        removeTmpUploadedFile();

        if (listeners != null) {
            boolean isInterrupt = false;
            if (event.getReason() != null) {
                if (event.getReason().getClass().getName().equals(FileUploadHandler.UploadInterruptedException.class.getName())) {
                    isInterrupt = true;
                }
            }
            if (listeners != null) {
                for (UploadListener l : listeners) {
                    l.uploadFailed(this, isInterrupt);
                }
            }
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        return createOutputStream();
    }
}
