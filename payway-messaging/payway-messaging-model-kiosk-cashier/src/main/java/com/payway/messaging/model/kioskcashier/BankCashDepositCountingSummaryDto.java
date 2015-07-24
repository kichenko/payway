/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.kioskcashier;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * BankCashDepositCountingSummaryDto
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BankCashDepositCountingSummaryDto extends AbstractDto {

    private static final long serialVersionUID = 5095746824860104810L;

    private final long nominalId;
    private final int quantity;
}
