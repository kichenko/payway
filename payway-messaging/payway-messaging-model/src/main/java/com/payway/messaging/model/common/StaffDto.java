/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.common;

import com.payway.messaging.model.IdentifiableDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * StaffDto
 *
 * @author Sergey Kichenko
 * @created 22.07.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class StaffDto extends IdentifiableDto {

    private static final long serialVersionUID = 1193728943301710455L;
    private final String name;

    public StaffDto(long id, String name) {
        super(id);
        this.name = name;
    }
}
