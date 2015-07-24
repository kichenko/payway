/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.kioskcashier.BanknoteNominalDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * CashDepositNominalsResponse
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class CashDepositNominalsResponse implements SuccessResponse {

    private static final long serialVersionUID = 4780143931680640223L;

    private final List<BanknoteNominalDto> nominals;
}
