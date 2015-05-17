/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service.app.user;

import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;

/**
 * UserAppService
 *
 * @author Sergey Kichenko
 * @created 10.05.15 00:00
 */
public interface UserAppService {

    public DbUser getUser();

    public boolean setUser(DbUser user);

    public DbConfiguration getConfiguration();

    public boolean setConfiguration(DbConfiguration config);
}
