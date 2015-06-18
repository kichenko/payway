/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.advertising;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.model.advertising.ApplyConfigurationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * AdvertisingApplyConfigurationRequest
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public final class AdvertisingApplyConfigurationRequest extends CommandRequest {

    private static final long serialVersionUID = -6600522755359031066L;

    private final ApplyConfigurationDto config;

}
