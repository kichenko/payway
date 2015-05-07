/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.payway.advertising.ui.view.workspace.content;

/**
 *
 * @author sergey
 */
public interface UploadListener {

    public void updateProgress(UploadTask task, long readBytes, long contentLength);

    public void uploadFailed(UploadTask task, boolean isInterrupted);

    public void uploadSucceeded(UploadTask task);
}
