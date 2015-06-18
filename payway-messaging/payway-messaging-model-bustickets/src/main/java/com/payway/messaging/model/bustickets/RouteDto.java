package com.payway.messaging.model.bustickets;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class RouteDto extends AbstractDto {

    private final String mnemonics;

    private final DirectionDto direction;

    private final String departureTime;

    private final double price;

    private final int seatsTotal;

    private final int seatsSold;

}
