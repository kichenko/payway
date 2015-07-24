/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.Getter;
import lombok.ToString;

/**
 * CashDepositNominalsRequest
 *
 * @author Sergey Kichenko
 * @created 23.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
public class CashDepositNominalsRequest extends CommandRequest {

    private static final long serialVersionUID = 6179276891732613573L;
}
