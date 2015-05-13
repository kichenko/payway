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
    private String path;
    private int bufSize;
    private long fileSize;
    private Upload upload;
    private String fileName;
    private String fileTmpExt;
    private boolean isInterrupted;
    private final List<UploadListener> listeners = new ArrayList<>();

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
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
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
    public void setTmpFileExt(String ext) {
        fileTmpExt = ext;
    }

    @Override
    public String getTmpFileExt() {
        return fileTmpExt;
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

        boolean isOk = false;

        //then upload is success - rename uploaded file (remove tmp file ext)
        try {
            File file = new File(getPath() + getFileName() + getTmpFileExt());
            isOk = file.renameTo(new File(getPath() + getFileName()));
            if (!isOk) {
                log.error("Error then rename uploaded file");
            }
        } catch (Exception ex) {
            log.error("", ex);
        }

        if (listeners != null) {
            if (isOk) {
                for (UploadListener l : listeners) {
                    l.uploadSucceeded(this);
                }
            } else {
                for (UploadListener l : listeners) {
                    l.uploadFailed(this, false);
                }
            }
        }
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        //then upload is failed, remove tmp uploaded file
        try {
            File file = new File(getPath() + getFileName() + getTmpFileExt());
            if (!file.delete()) {
                log.error("Error then delete failed uploaded file");
            }
        } catch (Exception ex) {
            log.error("", ex);
        }

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
        if (!isInterrupted()) {
            try {
                File file = new File(getPath() + getFileName() + getTmpFileExt());
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
