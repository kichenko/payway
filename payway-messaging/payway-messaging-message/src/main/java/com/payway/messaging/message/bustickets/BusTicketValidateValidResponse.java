package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.*;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
public class BusTicketValidateValidResponse implements SuccessResponse {

    String routeName;

    Double amount;

}
