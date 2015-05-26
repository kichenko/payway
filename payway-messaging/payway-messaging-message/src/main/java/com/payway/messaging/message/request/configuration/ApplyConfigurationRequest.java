/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.request.configuration;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.model.message.configuration.ApplyConfigurationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ApplyConfigurationRequest - request to apply local->server configuration
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ApplyConfigurationRequest extends CommandRequest {

    private static final long serialVersionUID = -6600522755359031066L;

    private ApplyConfigurationDto config;

}
