/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * EncashmentSaveBanknoteNominalRequest
 *
 * @author Sergey Kichenko
 * @created 06.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public class EncashmentSaveBanknoteNominalRequest extends CommandRequest {

    private static final long serialVersionUID = 2849838982105517376L;
}
