package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.message.IWebAppSessionAware;
import lombok.*;

/**
 * Created by mike on 05/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BusTicketPaymentStartRequest extends CommandRequest implements IWebAppSessionAware {

    private static final long serialVersionUID = 3834581793470282918L;

    String sessionId;

    long retailerTerminalId;

    long operatorId;

}
