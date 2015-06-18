package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
public class BusTicketValidateInvalidResponse implements SuccessResponse {

    private static final long serialVersionUID = -6005484352377529932L;

    String reason;

}
