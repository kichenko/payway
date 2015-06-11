package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.*;


/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusTicketOperatorsRequest extends CommandRequest {

    private static final long serialVersionUID = -2951927917496507300L;

}
