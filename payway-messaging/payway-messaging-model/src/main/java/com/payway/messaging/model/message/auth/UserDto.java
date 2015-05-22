/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.model.message.auth;

import com.payway.messaging.model.AbstractDto;
import com.payway.messaging.model.message.settings.SettingsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UserDto
 *
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends AbstractDto {

    private static final long serialVersionUID = 942608076596562119L;

    private String username;

    private String userToken;

    private boolean rememberMe;

}
