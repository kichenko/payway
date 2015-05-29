/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.ui.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * UIUtils
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UIUtils {

    public static String formatFileSize(long fileSize) {
        if (fileSize >= 1000000000) {
            return String.format("%.2f Gb", (fileSize / 1000000000.0));
        } else if (fileSize >= 1000000) {
            return String.format("%.2f Mb", (fileSize / 1000000.0));
        }
        return String.format("%.2f Kb", (fileSize / 1000.0));
    }
}
