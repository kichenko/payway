/*
 * (c) Payway, 2015. All right reserved.
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
