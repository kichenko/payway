package com.payway.messaging.model.bustickets;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class RouteDto extends AbstractDto {

    private static final long serialVersionUID = -7054019052878375518L;

    private final String mnemonics;

    private final DirectionDto direction;

    private final String departureTime;

    private final double price;

    private final int seatsTotal;

    private final int seatsSold;

    public RouteDto(String mnemonics, DirectionDto direction, String departureTime, double price, int seatsTotal, int seatsSold) {
        this.mnemonics = mnemonics;
        this.direction = direction;
        this.departureTime = departureTime;
        this.price = price;
        this.seatsTotal = seatsTotal;
        this.seatsSold = seatsSold;
    }

}
