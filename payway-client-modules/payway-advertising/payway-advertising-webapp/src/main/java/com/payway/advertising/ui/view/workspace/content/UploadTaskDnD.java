/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Html5File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * UploadTaskDnD
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
public class UploadTaskDnD implements UploadTask, StreamVariable {

    private String fileName;
    private UUID taskId;
    private UploadListener listener;
    private Html5File html5File;
    private boolean isInterrupted;

    public UploadTaskDnD() {
        taskId = UUID.randomUUID();
    }

    @Override
    public UUID getTaskId() {
        return taskId;
    }

    @Override
    public void setListener(UploadListener listener) {
        this.listener = listener;
    }

    @Override
    public void interrupt() {
        isInterrupted = true;
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
        if (listener != null) {
            listener.updateProgress(this, event.getBytesReceived(), event.getContentLength());
        }
    }

    @Override
    public void streamingFinished(StreamVariable.StreamingEndEvent event) {
        if (listener != null) {
            listener.uploadSucceeded(this);
        }
    }

    @Override
    public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
        if (listener != null) {
            listener.uploadFailed(this, this.isInterrupted);
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
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                //
            }
        };
    }
}
