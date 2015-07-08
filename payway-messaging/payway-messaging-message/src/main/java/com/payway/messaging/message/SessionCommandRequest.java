/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * SessionCommandRequest
 *
 * @author Sergey Kichenko
 * @created 08.07.15 00:00
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@ToString(callSuper = true)
public abstract class SessionCommandRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = 115523185498520164L;

    protected String sessionId;
}
