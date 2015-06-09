package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.common.ContentDto;
import com.payway.messaging.model.common.OperatorDto;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class BusTicketOperatorsResponse implements SuccessResponse {

    List<OperatorDto> operators = new LinkedList<>();

    public void add(String shortName, String name, ContentDto logo) {
        operators.add(new OperatorDto(shortName, name, logo));
    }

}
