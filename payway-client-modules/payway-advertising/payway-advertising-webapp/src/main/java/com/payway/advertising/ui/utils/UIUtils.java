/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.utils;

import com.payway.advertising.ui.component.ProgressBarWindow;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CrudEntityService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UIUtils {

    private final static ProgressBarWindow progressWindow = new ProgressBarWindow();

    public static void showLoadingIndicator() {
        progressWindow.show();
        UI.getCurrent().push();
    }

    public static void closeLoadingIndicator() {
        progressWindow.close();
        UI.getCurrent().push();
    }

    public static void showErrorNotification(String title, String message) {
        Notification.show(message, Notification.Type.ERROR_MESSAGE);
        UI.getCurrent().push();
    }

    public static void showWarningNotification(String title, String message) {
        Notification.show(message, Notification.Type.WARNING_MESSAGE);
        UI.getCurrent().push();
    }

    public static void showTrayNotification(String title, String message) {
        Notification.show(message, Notification.Type.TRAY_NOTIFICATION);
        UI.getCurrent().push();
    }

    public static String formatFileSize(long fileSize) {
        if (fileSize >= 1000000000) {
            return String.format("%.2f Gb", (fileSize / 1000000000.0));
        } else if (fileSize >= 1000000) {
            return String.format("%.2f Mb", (fileSize / 1000000.0));
        }
        return String.format("%.2f Kb", (fileSize / 1000.0));
    }
}
