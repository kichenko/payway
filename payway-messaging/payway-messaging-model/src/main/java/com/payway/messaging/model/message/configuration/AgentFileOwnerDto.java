/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.message.configuration;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AgentFileOwnerDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentFileOwnerDto extends AbstractDto {

    private static final long serialVersionUID = 6760249542483546076L;

    protected String name;
    protected String description;

    public AgentFileOwnerDto(long id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }
}
