package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.bustickets.DirectionDto;
import com.payway.messaging.model.bustickets.RouteDto;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 05/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
public class BusTicketPaymentStartResponse implements SuccessResponse {

    List<DirectionDto> directions = new LinkedList<>();

    public DirectionDto addDirection(String mnemonics, String name) {
        DirectionDto dd = new DirectionDto(mnemonics, name);
        directions.add(dd);
        return dd;
    }

    List<RouteDto> routes = new ArrayList<>();

    public RouteDto addRoute(String mnemonics, DirectionDto direction, String time, int seatsTotal, int seatsSold, double price) {
        RouteDto rd = new RouteDto(mnemonics, direction, time, price, seatsTotal, seatsSold);
        routes.add(rd);
        return rd;
    }

}
