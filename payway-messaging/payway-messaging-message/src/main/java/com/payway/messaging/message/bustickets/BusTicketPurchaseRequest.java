package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.*;

import java.util.Date;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusTicketPurchaseRequest extends CommandRequest {

    private static final long serialVersionUID = 4144648146040100002L;

    String localTxId;

    String sessionId;

    long retailerTerminalId;

    String serviceProviderId;

    String contactNumber;

    String dateId;

    String routeId;

    String baggageId;

    int quantity;

    Date paymentStart;

    Date paymentStop;

}
