/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.ToString;

/**
 *
 * SessionCommandRequest
 *
 * @author Sergey Kichenko
 * @created 08.07.15 00:00
 */
@ToString(callSuper = true)
public abstract class SessionCommandRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = 115523185498520164L;

    protected String sessionId;

    protected SessionCommandRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

}
