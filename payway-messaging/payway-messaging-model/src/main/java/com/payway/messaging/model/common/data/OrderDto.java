/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.common.data;

import com.payway.messaging.model.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * OrderDto
 *
 * @author Sergey Kichenko
 * @created 21.07.15 00:00
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class OrderDto extends AbstractDto {

    private static final long serialVersionUID = -7294779775151825144L;

    private final boolean ascending;
    private final String field;
}
