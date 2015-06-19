/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.common;

/**
 * MoneyPrecisionDto
 *
 * @author Sergey Kichenko
 * @created 19.06.15 00:00
 */
public enum MoneyPrecisionDto {

    Auto(true), Zero(false), Two(true);

    //For POS pay points
    boolean allowDecimalSum;

    public boolean isAllowDecimalSum() {
        return allowDecimalSum;
    }

    MoneyPrecisionDto(boolean allowDecimalSum) {
        this.allowDecimalSum = allowDecimalSum;
    }
}
