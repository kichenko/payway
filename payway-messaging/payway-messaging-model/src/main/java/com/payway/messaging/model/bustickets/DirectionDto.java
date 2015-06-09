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
public class DirectionDto extends AbstractDto {

    String mnemonics;

    String name;

}
