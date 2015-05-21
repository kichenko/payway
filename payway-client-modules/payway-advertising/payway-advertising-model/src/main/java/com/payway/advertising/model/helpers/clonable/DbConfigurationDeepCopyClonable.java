/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model.helpers.clonable;

import com.payway.advertising.model.DbConfiguration;

/**
 * DbConfigurationDeepCopyClonable
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public class DbConfigurationDeepCopyClonable implements EntityClonable<DbConfiguration> {

    @Override
    public DbConfiguration clone(DbConfiguration config) {
        return new DbConfiguration(config.getId(), config.getName(), config.getUser() != null ? new DbUserDeepCopyClonable().clone(config.getUser()) : null);
    }
}
