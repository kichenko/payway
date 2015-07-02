/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.commons.webapp.service.app.user;

import com.payway.messaging.model.user.UserDto;

/**
 * UserAppService
 *
 * @author Sergey Kichenko
 * @created 01.07.15 00:00
 */
public interface UserAppService {

    UserDto getUser();

    boolean setUser(UserDto user);

}
