/*
 * (c) Sergey Kichenko, 2015. All right reserved.
 */
package com.payway.advertising.core.service.user;

import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;

/**
 * UserService
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public interface UserService {

    public DbUser getUser();

    public boolean setUser(DbUser user);

    public DbConfiguration getConfiguration();

    public boolean setDbConfiguration(DbConfiguration config);
}
