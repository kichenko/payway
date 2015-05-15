/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Html5File;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * UploadTaskDnD
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
@Slf4j
public class UploadTaskDnD implements UploadTask, StreamVariable {

    private UUID taskId;
    private int bufSize;
    private String path;
    private long fileSize;
    private String fileName;
    private String fileTmpExt;
    private Html5File html5File;
    private boolean isInterrupted;
    private final List<UploadListener> listeners = new ArrayList<>();

    public UploadTaskDnD() {
        taskId = UUID.randomUUID();
        bufSize = 2048;
    }

    public UploadTaskDnD(String path, int bufSize) {
        this();
        this.path = path;
        this.bufSize = bufSize;
    }

    @Override
    public UUID getTaskId() {
        return taskId;
    }

    @Override
    public void addListener(UploadListener listener) {
        listeners.add(listener);
    }

    @Override
    public void interrupt() {
        isInterrupted = true;
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
    public String getFileName() {
        return fileName;
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
    public void setUploadObject(Object uploadObject) {
        html5File = (Html5File) uploadObject;
        if (html5File != null) {
            html5File.setStreamVariable(this);
        }
    }

    @Override
    public Object getUploadObject() {
        return html5File;
    }

    @Override
    public boolean listenProgress() {
        return true;
    }

    @Override
    public void onProgress(StreamVariable.StreamingProgressEvent event) {
        if (listeners != null) {
            for (UploadListener l : listeners) {
                l.updateProgress(this, event.getBytesReceived(), event.getContentLength());
            }
        }
    }

    @Override
    public void streamingFinished(StreamVariable.StreamingEndEvent event) {

        boolean isOk = false;

        //then upload is success - rename uploaded file (remove tmp file ext)
        try {
            Thread.sleep(2000);
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
    public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
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
            for (UploadListener l : listeners) {
                l.uploadFailed(this, this.isInterrupted);
            }
        }
    }

    @Override
    public boolean isInterrupted() {
        return isInterrupted;
    }

    @Override
    public void streamingStarted(StreamVariable.StreamingStartEvent event) {
        //
    }

    @Override
    public OutputStream getOutputStream() {
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
