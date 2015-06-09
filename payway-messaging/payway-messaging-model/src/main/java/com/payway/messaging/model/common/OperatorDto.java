package com.payway.messaging.model.common;

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
public class OperatorDto extends AbstractDto {

    String shortName;

    String name;

    ContentDto logo;

}
