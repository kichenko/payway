/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.ui.component.FileUploadPanel;
import com.payway.advertising.ui.component.UploadButtonWrapper;
import com.payway.advertising.ui.component.UploadTaskPanel;
import com.payway.commons.webapp.ui.view.core.WorkspaceView;

/**
 * AdvertisingWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public interface AdvertisingWorkspaceView extends WorkspaceView {

    void setFileUploadPanel(FileUploadPanel fileUploadPanel);

    FileUploadPanel getFileUploadPanel();

    void setUploadTaskPanel(UploadTaskPanel uploadTaskPanel);

    UploadTaskPanel getUploadTaskPanel();

    void setButtonFileUploadToolBar(UploadButtonWrapper btnFileUploadToolBar);

    UploadButtonWrapper getButtonFileUploadToolBar();
}
