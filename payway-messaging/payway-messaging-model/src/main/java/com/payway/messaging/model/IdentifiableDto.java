package com.payway.messaging.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by mike on 08/07/15.
 */
@Getter
@ToString(callSuper = true)
public abstract class IdentifiableDto extends AbstractDto {

    private static final long serialVersionUID = 5143947813111476369L;

    final private long id;

    protected IdentifiableDto(long id) {
        this.id = id;
    }
}
