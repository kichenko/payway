package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.message.IWebAppSessionAware;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * Created by mike on 09/06/15.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusTicketOperatorsRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = -2951927917496507300L;

    String sessionId;

    long retailerTerminalId;

    public BusTicketOperatorsRequest(String sessionId, long retailerTerminalId) {
        this.sessionId = sessionId;
        this.retailerTerminalId = retailerTerminalId;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

}
