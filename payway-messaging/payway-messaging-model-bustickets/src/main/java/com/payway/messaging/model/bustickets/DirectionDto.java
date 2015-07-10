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
public final class DirectionDto extends AbstractDto {

    private static final long serialVersionUID = -263792822840700164L;

    private final String mnemonics;

    private final String name;

    public DirectionDto(String mnemonics, String name) {
        this.mnemonics = mnemonics;
        this.name = name;
    }

}
