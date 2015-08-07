package com.payway.messaging.message.bustickets;

import com.payway.messaging.core.response.SuccessResponse;
import com.payway.messaging.model.bustickets.DirectionDto;
import com.payway.messaging.model.bustickets.RouteDto;
import com.payway.messaging.model.common.ChoiceDto;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mike on 05/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper = true)
public class BusTicketPaymentStartResponse implements SuccessResponse {

    private static final long serialVersionUID = -4078176497129051908L;

    List<DirectionDto> directions = new LinkedList<>();

    List<RouteDto> routes = new LinkedList<>();

    List<ChoiceDto> dates = new LinkedList<>();

    public DirectionDto addDirection(String mnemonics, String name) {
        DirectionDto dd = new DirectionDto(mnemonics, name);
        directions.add(dd);
        return dd;
    }

    public RouteDto addRoute(String mnemonics, DirectionDto direction, String time, int seatsTotal, int seatsSold, double price) {
        RouteDto rd = new RouteDto(mnemonics, direction, time, price, seatsTotal, seatsSold);
        routes.add(rd);
        return rd;
    }

    public boolean addTripDate(String mnemonics, String label) {
        return dates.add(new ChoiceDto(mnemonics, label));
    }
}
