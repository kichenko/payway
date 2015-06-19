package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.message.IWebAppSessionAware;
import lombok.*;


/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusTicketOperatorsRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = -2951927917496507300L;

    String sessionId;

    long retailerTerminalId;

}
