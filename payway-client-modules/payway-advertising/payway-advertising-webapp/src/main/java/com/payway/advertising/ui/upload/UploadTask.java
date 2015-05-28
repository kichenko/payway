/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.upload;

import java.util.UUID;

/**
 * UploadTask
 *
 * @author Sergey Kichenko
 * @created 07.05.15 00:00
 */
public interface UploadTask {

    void interrupt();

    boolean isInterrupted();

    UUID getTaskId();

    void setFileName(String fileName);

    String getFileName();

    void setTmpFileExt(String ext);

    String getTmpFileExt();

    void setPath(String path);

    String getPath();

    int getBufferSize();

    void setBufferSize(int size);

    long getFileSize();

    void setFileSize(long fileSize);

    void addListener(UploadListener listener);

    void setUploadObject(Object uploadObject);

    Object getUploadObject();
}
