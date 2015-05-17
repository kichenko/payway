/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.view.core;

import com.payway.advertising.ui.component.ProgressBarWindow;
import com.payway.advertising.ui.component.UploadTaskPanel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * AbstractWorkspaceView
 *
 * @author Sergey Kichenko
 * @created 17.05.15 00:00
 */
public abstract class AbstractWorkspaceView extends VerticalLayout implements WorkspaceView {

    protected UploadTaskPanel uploadTaskPanel;
    protected ProgressBarWindow progressBarWindow;

    @Override
    public void setUploadTaskPanel(UploadTaskPanel uploadTaskPanel) {
        this.uploadTaskPanel = uploadTaskPanel;
    }

    @Override
    public UploadTaskPanel getUploadTaskPanel() {
        return uploadTaskPanel;
    }

    @Override
    public void setProgressBarWindow(ProgressBarWindow progressBarWindow) {
        this.progressBarWindow = progressBarWindow;
    }

    @Override
    public ProgressBarWindow getProgressBarWindow() {
        return progressBarWindow;
    }

    @Override
    public void showProgressBar() {
        progressBarWindow.show();
        UI.getCurrent().push();
    }

    @Override
    public void hideProgressBar() {
        progressBarWindow.close();
        UI.getCurrent().push();
    }
}
