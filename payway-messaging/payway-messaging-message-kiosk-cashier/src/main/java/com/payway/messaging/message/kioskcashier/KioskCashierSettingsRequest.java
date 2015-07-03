/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * KioskCashierSettingsRequest
 *
 * @author Sergey Kichenko
 * @created 03.07.15 00:00
 */
@NoArgsConstructor
@ToString(callSuper = true)
public final class KioskCashierSettingsRequest extends CommandRequest {

    private static final long serialVersionUID = 3131799104289428802L;

}
