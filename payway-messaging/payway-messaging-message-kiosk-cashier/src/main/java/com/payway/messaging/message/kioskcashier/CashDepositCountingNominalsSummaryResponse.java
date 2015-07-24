/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.kioskcashier.BankCashDepositCountingSummaryDto;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * CashDepositCountingNominalsSummaryResponse
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class CashDepositCountingNominalsSummaryResponse implements SuccessResponse {

    private static final long serialVersionUID = -7367784028267773504L;

    private final List<BanknoteNominalDto> nominals;
    private final List<BankCashDepositCountingSummaryDto> countingSummary;

}
