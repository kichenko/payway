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

    private static final long serialVersionUID = -5820831317958391388L;

    private String shortName;

    private String name;

    private ContentDto logo;

}
