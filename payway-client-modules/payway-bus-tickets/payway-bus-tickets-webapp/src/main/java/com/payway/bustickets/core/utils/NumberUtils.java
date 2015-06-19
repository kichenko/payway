/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * NumberUtils
 *
 * @author sergey kichenko
 * @created 19.06.15 00:00
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberUtils {

    public static boolean isInteger(double number) {
        return number % 1 == 0;
    }
}
