/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.model.User;

/**
 * UserService
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public interface UserService {

    public User getUser();

    public boolean setUser(User user);
}
