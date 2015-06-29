/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.upload;

import com.vaadin.ui.UI;
import java.lang.ref.WeakReference;
import lombok.NoArgsConstructor;

/**
 * AbstractFileUploadedProcessorTaskListener
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@NoArgsConstructor
public abstract class AbstractFileUploadedProcessorTaskListener implements FileUploadedProcessorTaskListener {

    private WeakReference<UI> ui;
    private WeakReference<UploadTask> uploadTask;
    private volatile boolean interrupt;

    public AbstractFileUploadedProcessorTaskListener(final UploadTask uploadTask, final UI ui) {
        this.ui = new WeakReference(ui);
        this.uploadTask = new WeakReference(uploadTask);
    }

    protected UI getUIRef() {
        return ui.get();
    }

    protected UploadTask getUploadTaskRef() {
        return uploadTask.get();
    }

    protected void access(Runnable runnable) {

        final UI uI = this.getUIRef();
        final UploadTask upldTask = uploadTask.get();

        if (uI != null && upldTask != null && runnable != null) {
            uI.access(runnable);
        }
    }

    @Override
    public boolean isInterrupt() {
        return interrupt;
    }

    @Override
    public void interrupt() {
        this.interrupt = true;
    }

}
