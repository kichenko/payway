/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.advertising;

import com.payway.messaging.model.IdentifiableDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * AgentFileOwnerDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class AgentFileOwnerDto extends IdentifiableDto {

    private static final long serialVersionUID = 6760249542483546076L;

    protected String name;

    protected String description;

    public AgentFileOwnerDto(long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

}
