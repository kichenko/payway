package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.message.IWebAppSessionAware;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusTicketPurchaseRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = 4144648146040100002L;

    String localTxId;

    String sessionId;

    long retailerTerminalId;

    long operatorId;

    String contactNumber;

    String dateId;

    String routeId;

    String baggageId;

    int quantity;

    Date paymentStart;

    Date paymentStop;

    double amount;

    public BusTicketPurchaseRequest(String localTxId, String sessionId, long retailerTerminalId, long operatorId, String contactNumber, String dateId, String routeId, String baggageId, int quantity, Date paymentStart, Date paymentStop, double amount) {
        this.localTxId = localTxId;
        this.sessionId = sessionId;
        this.retailerTerminalId = retailerTerminalId;
        this.operatorId = operatorId;
        this.contactNumber = contactNumber;
        this.dateId = dateId;
        this.routeId = routeId;
        this.baggageId = baggageId;
        this.quantity = quantity;
        this.paymentStart = paymentStart;
        this.paymentStop = paymentStop;
        this.amount = amount;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

}
