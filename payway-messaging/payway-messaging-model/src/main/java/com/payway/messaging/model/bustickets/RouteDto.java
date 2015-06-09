package com.payway.messaging.model.bustickets;

import com.payway.messaging.model.AbstractDto;
import lombok.*;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RouteDto extends AbstractDto {

    String mnemonics;

    DirectionDto direction;

    String departureTime;

    double price;

    int seatsTotal;

    int seatsSold;

}
