/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Helpers
 *
 * @author Sergey Kichenko
 * @created 23.05.15 00:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Helpers {

    public static String addSeparator2End(String value) {
        if (!StringUtils.isBlank(value)) {
            if (value.charAt(value.length() - 1) != "/".charAt(0)) {
                return value + "/";
            }
        }
        return value;
    }
}
