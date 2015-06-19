/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.bustickets.SettingsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * BusTicketSettingsResponse
 *
 * @author Sergey Kichenko
 * @created 18.06.15 00:00
 */
@Getter
@ToString
@AllArgsConstructor
public final class BusTicketSettingsResponse implements SuccessResponse {

    private static final long serialVersionUID = -3669968098990639785L;

    private final SettingsDto settings;
}
