/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.payway.advertising.ui.view.workspace.content;

import java.util.UUID;

/**
 *
 * @author sergey
 */
public interface UploadTask {

    void interrupt();

    UUID getTaskId();

    String getFileName();

    void setFileName(String fileName);

    void setListener(UploadListener listener);

    void setUploadObject(Object uploadObject);

    Object getUploadObject();
}
