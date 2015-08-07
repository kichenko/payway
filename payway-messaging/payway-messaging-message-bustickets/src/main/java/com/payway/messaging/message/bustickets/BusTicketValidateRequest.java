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
public class BusTicketValidateRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = 4144648146040100002L;

    String sessionId;

    long retailerTerminalId;

    long operatorId;

    String contactNumber;

    String dateId;

    String routeId;

    int baggage;

    int quantity;

    public BusTicketValidateRequest(String sessionId, long retailerTerminalId, long operatorId, String contactNumber, String dateId, String routeId, int baggage, int quantity) {
        this.sessionId = sessionId;
        this.retailerTerminalId = retailerTerminalId;
        this.operatorId = operatorId;
        this.contactNumber = contactNumber;
        this.dateId = dateId;
        this.routeId = routeId;
        this.baggage = baggage;
        this.quantity = quantity;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

}
