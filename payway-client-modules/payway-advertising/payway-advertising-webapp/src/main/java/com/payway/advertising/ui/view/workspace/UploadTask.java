/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.payway.advertising.ui.view.workspace;

import com.vaadin.ui.Upload;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author sergey
 */
@Getter
@Setter
public class UploadTask implements Upload.StartedListener, Upload.ProgressListener, Upload.FailedListener, Upload.SucceededListener, Upload.FinishedListener {

    public interface UploadListener extends Serializable {

        public void uploadStarted(UploadTask task, String fileName);

        public void uploadFinished(UploadTask task);

        public void uploadFailed(UploadTask task);

        public void uploadSucceeded(UploadTask task);

        public void updateProgress(UploadTask task, long readBytes, long contentLength);
    }

    private Object taskId;
    private Upload upload;
    private UploadListener listener;

    public UploadTask(Upload upload, UploadListener listener, Object taskId) {

        this.upload = upload;
        this.listener = listener;
        this.taskId = taskId;

        this.upload.addStartedListener(this);
        this.upload.addProgressListener(this);
        this.upload.addFailedListener(this);
        this.upload.addSucceededListener(this);
        this.upload.addFinishedListener(this);
    }

    @Override
    public void uploadStarted(Upload.StartedEvent event) {
        if (listener != null) {
            listener.uploadStarted(this, event.getFilename());
        }
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
            listener.uploadFailed(this);
        }
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        if (listener != null) {
            listener.uploadSucceeded(this);
        }
    }

    @Override
    public void uploadFinished(Upload.FinishedEvent event) {
        if (listener != null) {
            listener.uploadFinished(this);
        }
    }
}
