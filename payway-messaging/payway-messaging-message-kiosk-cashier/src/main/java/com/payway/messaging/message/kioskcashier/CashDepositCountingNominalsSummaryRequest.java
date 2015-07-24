/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * CashDepositCountingNominalsSummaryRequest
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public class CashDepositCountingNominalsSummaryRequest extends CommandRequest {

    private static final long serialVersionUID = 4206165875578990666L;

    private final List<Long> countingIds;
}
