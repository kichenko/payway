/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.message.auth;

import com.payway.messaging.model.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * UserDto
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@ToString(callSuper = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends AbstractDto {

    private static final long serialVersionUID = 942608076596562119L;

    final private String username;

    final private String firstName;

    final private String lastName;

}
