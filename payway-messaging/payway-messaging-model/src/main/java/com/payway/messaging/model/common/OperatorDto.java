package com.payway.messaging.model.common;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 09/06/15.
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class OperatorDto extends AbstractDto {

    private static final long serialVersionUID = -5820831317958391388L;

    private final String shortName;

    private final String name;

    private final ContentDto logo;

}
