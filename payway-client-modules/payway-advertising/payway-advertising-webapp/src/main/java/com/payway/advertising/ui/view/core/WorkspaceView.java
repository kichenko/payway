/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.ui.component.FileUploadPanel;
import com.payway.advertising.ui.component.ProgressBarWindow;
import com.payway.advertising.ui.component.UploadButtonWrapper;
import com.payway.advertising.ui.component.UploadTaskPanel;
import com.vaadin.ui.MenuBar;

/**
 * WorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public interface WorkspaceView {

    void activate();

    void setMenuBar(MenuBar menuBar);

    MenuBar getMenuBar();

    void setFileUploadPanel(FileUploadPanel fileUploadPanel);

    FileUploadPanel getFileUploadPanel();

    void setUploadTaskPanel(UploadTaskPanel uploadTaskPanel);

    UploadTaskPanel getUploadTaskPanel();

    void setProgressBarWindow(ProgressBarWindow progressBarWindow);

    ProgressBarWindow getProgressBarWindow();

    void showProgressBar();

    void hideProgressBar();

    void setButtonFileUploadToolBar(UploadButtonWrapper btnFileUploadToolBar);

    UploadButtonWrapper getButtonFileUploadToolBar();
}
