/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ChooseNoteCountingsNotInDepositRequest
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public final class CreateBankCashDepositParamsRequest extends CommandRequest {

    private static final long serialVersionUID = -1919645118059342470L;
}
