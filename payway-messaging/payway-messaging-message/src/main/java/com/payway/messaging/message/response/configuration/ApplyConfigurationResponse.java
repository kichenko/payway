/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.response.configuration;

import com.payway.messaging.message.response.auth.AbstractAuthCommandResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ApplyConfigurationResponse - response on reques of appling server
 * configuration
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApplyConfigurationResponse extends AbstractAuthCommandResponse {

    private static final long serialVersionUID = 8810560983127046283L;

    private boolean success;
}
