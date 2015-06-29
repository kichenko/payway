/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.upload;

/**
 * FileUploadedProcessorTaskListener
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public interface FileUploadedProcessorTaskListener {

    void onStart(final int countTask);

    void onProcess(final int currentTask, final int countTask);

    void onFail(final int currentTask, final int countTask);

    void onFinish();

    void onInterrupt();

    boolean isInterrupt();

    void interrupt();
}
