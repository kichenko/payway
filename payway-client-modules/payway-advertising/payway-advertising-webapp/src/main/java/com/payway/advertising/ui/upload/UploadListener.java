/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.upload;


/**
 * UploadListener
 *
 * @author Sergey Kichenko
 * @created 07.05.15 00:00
 */
public interface UploadListener {

    public void updateProgress(final UploadTask task, long readBytes, long contentLength);

    public void uploadFailed(final UploadTask task, boolean isInterrupted);

    public void uploadSucceeded(final UploadTask task);
}
