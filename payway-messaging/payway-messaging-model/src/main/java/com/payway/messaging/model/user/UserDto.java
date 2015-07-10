/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.user;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * UserDto
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class UserDto extends AbstractDto {

    private static final long serialVersionUID = 942608076596562119L;

    final private String username;

    final private String firstName;

    final private String lastName;

    public UserDto(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
