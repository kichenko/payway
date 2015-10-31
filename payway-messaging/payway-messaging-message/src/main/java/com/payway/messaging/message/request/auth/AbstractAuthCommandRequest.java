/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.request.auth;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * AbstractAuthCommandRequest
 *
 * @author Sergey Kichenko
 * @created 17.07.15 00:00
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAuthCommandRequest extends CommandRequest {

    private static final long serialVersionUID = -1960655164478346610L;

    private String appId;

    private String remoteAddress;
}
