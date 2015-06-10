/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.ui.component.FileUploadPanel;
import com.payway.advertising.ui.component.UploadButtonWrapper;
import com.payway.advertising.ui.component.UploadTaskPanel;
import com.payway.commons.webapp.ui.view.core.AbstractWorkspaceView;

/**
 * AbstractAdvertisingWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public abstract class AbstractAdvertisingWorkspaceView extends AbstractWorkspaceView implements AdvertisingWorkspaceView {

    protected UploadTaskPanel uploadTaskPanel;
    protected FileUploadPanel fileUploadPanel;
    protected UploadButtonWrapper btnFileUploadToolBar;

    @Override
    public void setFileUploadPanel(FileUploadPanel fileUploadPanel) {
        this.fileUploadPanel = fileUploadPanel;
    }

    @Override
    public FileUploadPanel getFileUploadPanel() {
        return fileUploadPanel;
    }

    @Override
    public void setUploadTaskPanel(UploadTaskPanel uploadTaskPanel) {
        this.uploadTaskPanel = uploadTaskPanel;
    }

    @Override
    public UploadTaskPanel getUploadTaskPanel() {
        return uploadTaskPanel;
    }

    @Override
    public void setButtonFileUploadToolBar(UploadButtonWrapper btnFileUploadToolBar) {
        this.btnFileUploadToolBar = btnFileUploadToolBar;

    }

    @Override
    public UploadButtonWrapper getButtonFileUploadToolBar() {
        return btnFileUploadToolBar;
    }
}
