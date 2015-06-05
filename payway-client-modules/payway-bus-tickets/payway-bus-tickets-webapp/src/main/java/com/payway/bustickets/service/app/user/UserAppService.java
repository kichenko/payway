/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.bustickets.service.app.user;

import com.payway.messaging.model.message.auth.UserDto;

/**
 * UserAppService
 *
 * @author Sergey Kichenko
 * @created 06.06.15 00:00
 */
public interface UserAppService {

    public UserDto getUser();

    public boolean setUser(UserDto user);
}
