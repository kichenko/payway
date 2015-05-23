/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import com.vaadin.server.StreamVariable;
import com.vaadin.ui.Html5File;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * UploadTaskDnD
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
@Slf4j
public class UploadTaskDnD extends AbstractUploadTask implements StreamVariable {

    private Html5File html5File;

    public UploadTaskDnD() {
        super();
    }

    public UploadTaskDnD(String path, int bufSize) {
        super(path, bufSize);
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
        //then upload is success, rename tmp uploaded file to original
        uploadFinished();
    }

    @Override
    public void streamingFailed(StreamVariable.StreamingErrorEvent event) {
        //then upload is failed, remove tmp uploaded file
        removeTmpUploadedFile();

        if (listeners != null) {
            for (UploadListener l : listeners) {
                l.uploadFailed(this, this.isInterrupted);
            }
        }
    }

    @Override
    public void streamingStarted(StreamVariable.StreamingStartEvent event) {
        //
    }

    @Override
    public OutputStream getOutputStream() {
        return createOutputStream();
    }
}
