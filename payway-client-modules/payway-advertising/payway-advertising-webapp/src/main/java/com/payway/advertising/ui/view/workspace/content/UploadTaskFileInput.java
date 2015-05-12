/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.server.communication.FileUploadHandler;
import com.vaadin.ui.Upload;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * UploadTaskFileInput
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
@Slf4j
public class UploadTaskFileInput implements UploadTask, Upload.Receiver, Upload.ProgressListener, Upload.FailedListener, Upload.SucceededListener {

    private UUID taskId;
    private Upload upload;
    private String fileName;
    private String path;
    private int bufSize;
    private boolean isInterrupted;
    private List<UploadListener> listeners = new ArrayList<>();

    public UploadTaskFileInput() {
        taskId = UUID.randomUUID();
        bufSize = 2048;
    }

    public UploadTaskFileInput(String path, int bufSize) {
        this();
        this.path = path;
        this.bufSize = bufSize;
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
    public boolean isInterrupted() {
        return isInterrupted;
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
    public String getPath() {
        return path;
    }

    @Override
    public int getBufferSize() {
        return bufSize;
    }

    @Override
    public void setBufferSize(int bufSize) {
        this.bufSize = bufSize;
    }

    @Override
    public void addListener(UploadListener listener) {
        listeners.add(listener);
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
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
    public void uploadFailed(Upload.FailedEvent event
    ) {
        if (listeners != null) {
            boolean isInterrupted = false;
            if (event.getReason() != null) {
                if (event.getReason().getClass().getName().equals(FileUploadHandler.UploadInterruptedException.class.getName())) {
                    isInterrupted = true;
                }
            }
            if (listeners != null) {
                for (UploadListener l : listeners) {
                    l.uploadFailed(this, isInterrupted);
                }
            }
        }
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        if (listeners != null) {
            if (listeners != null) {
                for (UploadListener l : listeners) {
                    l.uploadSucceeded(this);
                }
            }
        }
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        if (!isInterrupted()) {
            try {
                File file = new File(getPath() + getFileName());
                if (!file.exists()) {
                    return new BufferedOutputStream(new FileOutputStream(file), getBufferSize());
                } else {
                    interrupt();
                }
            } catch (Exception ex) {
                log.error("Error create output stream", ex);
            }
        }

        return new NullOutputStream();
    }
}
