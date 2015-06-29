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

    void setUploadPath(String path);

    String getUploadPath();

    int getBufferSize();

    void setBufferSize(int size);

    long getFileSize();

    void setFileSize(long fileSize);

    void addListener(UploadListener listener);

    void setUploadObject(Object uploadObject);

    Object getUploadObject();

    String getUploadTempFileName();

    void setUploadTempFileName(String fileName);

    FileUploadedProcessorTaskListener getFileUploadedProcessorTaskListener();

    void setFileUploadedProcessorTaskListener(FileUploadedProcessorTaskListener callback);

    void setDestFilePath(String path);

    String getDestFilePath();
}
