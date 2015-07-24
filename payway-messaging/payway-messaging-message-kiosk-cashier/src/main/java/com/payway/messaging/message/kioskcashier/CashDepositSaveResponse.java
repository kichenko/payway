/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * CashDepositSaveResponse
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class CashDepositSaveResponse implements SuccessResponse {

    private static final long serialVersionUID = -5705118773015313974L;

}
