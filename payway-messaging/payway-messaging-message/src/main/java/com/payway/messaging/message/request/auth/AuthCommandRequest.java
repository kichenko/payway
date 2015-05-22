/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.request.auth;

import com.payway.messaging.core.request.command.CommandRequest;
import com.payway.messaging.model.message.auth.UserDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthCommandRequest extends CommandRequest {

    private static final long serialVersionUID = -8380831312004043667L;

    private String userName;

    private String password;

}
