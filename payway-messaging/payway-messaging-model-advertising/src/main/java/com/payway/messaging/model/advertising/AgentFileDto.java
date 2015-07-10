/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.advertising;

import com.payway.messaging.model.AbstractDto;
import lombok.*;

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
