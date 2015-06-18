/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * BusTicketSettingsRequest
 *
 * @author Sergey Kichenko
 * @created 18.06.15 00:00
 */
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class BusTicketSettingsRequest extends CommandRequest {

    private static final long serialVersionUID = -3121844959017284486L;

}
