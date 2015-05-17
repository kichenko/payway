/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.ui.component.ProgressBarWindow;
import com.payway.advertising.ui.component.UploadTaskPanel;

/**
 * WorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public interface WorkspaceView {

    void activate();

    void setUploadTaskPanel(UploadTaskPanel uploadTaskPanel);

    UploadTaskPanel getUploadTaskPanel();

    void setProgressBarWindow(ProgressBarWindow progressBarWindow);

    ProgressBarWindow getProgressBarWindow();

    void showProgressBar();
    void hideProgressBar();
}
