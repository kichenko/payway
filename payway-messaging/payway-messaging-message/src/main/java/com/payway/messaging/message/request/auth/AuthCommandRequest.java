/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.messaging.message.request.auth;

import com.payway.messaging.core.request.command.CommandRequest;
import lombok.*;

/**
 * @author Sergey Kichenko
 * @created 23.04.15 00:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = "password")
@EqualsAndHashCode(callSuper = true)
public class AuthCommandRequest extends CommandRequest {

    private static final long serialVersionUID = -8380831312004043667L;

    private String userName;

    private String password;

    private String appId;

}
