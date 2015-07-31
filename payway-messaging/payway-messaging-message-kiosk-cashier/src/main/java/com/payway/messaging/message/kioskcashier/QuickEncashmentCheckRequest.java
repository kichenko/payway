/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.kioskcashier;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * QuickEncashmentCheckRequest
 *
 * @author Sergey Kichenko
 * @created 30.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class QuickEncashmentCheckRequest extends CommandRequest {

    private static final long serialVersionUID = 1883293760547607095L;

    private final long encashmentId;
}
