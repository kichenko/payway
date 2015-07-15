/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.model.kioskcashier.CountingDiscrepancyDto;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentCountingDiscrepancySaveRequest
 *
 * @author Sergey Kichenko
 * @created 14.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public final class EncashmentCountingDiscrepancySaveRequest extends CommandRequest {

    private static final long serialVersionUID = -2481809988234053018L;

    private final long countingId;
    private final List<CountingDiscrepancyDto> discrepancies;

    public EncashmentCountingDiscrepancySaveRequest(long countingId, List<CountingDiscrepancyDto> discrepancies) {
        this.countingId = countingId;
        this.discrepancies = discrepancies;
    }
}
