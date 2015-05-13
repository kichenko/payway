/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.workspace.content;

import java.util.UUID;

/**
 * UploadTask
 *
 * @author Sergey Kichenko
 * @created 07.10.15 00:00
 */
public interface UploadTask {

    void interrupt();

    boolean isInterrupted();

    UUID getTaskId();

    String getFileName();

    String getPath();

    int getBufferSize();

    void setBufferSize(int size);

    void setFileName(String fileName);

    void setPath(String path);

    long getFileSize();
    void setFileSize(long fileSize);

    void addListener(UploadListener listener);

    void setUploadObject(Object uploadObject);

    Object getUploadObject();
}
