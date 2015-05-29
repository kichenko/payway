/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.message.configuration;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * AgentFileDto
 *
 * @author Sergey Kichenko
 * @created 19.05.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AgentFileDto extends AbstractDto {

    private static final long serialVersionUID = -8855396010847579468L;

    protected String name;
    protected DbFileTypeDto kind;
    protected long ownerId;
    protected String expression;
    protected String digest;
    protected boolean isCountHits;
    protected int seqNo;
}
