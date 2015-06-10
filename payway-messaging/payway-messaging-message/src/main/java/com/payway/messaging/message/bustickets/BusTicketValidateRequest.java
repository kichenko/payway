package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.*;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusTicketValidateRequest extends CommandRequest {

    String serviceProviderId;

    String contactNumber;

    String dateId;

    String routeId;

    String baggageId;

    int quantity;

}
