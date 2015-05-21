/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model.helpers.clonable;

import com.payway.advertising.model.DbUser;

/**
 * DbUserDeepCopyFunction
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public class DbUserDeepCopyClonable implements EntityClonable<DbUser> {

    @Override
    public DbUser clone(DbUser user) {
        return new DbUser(user.getId(), user.getLogin(), user.getPassword(), user.getToken());
    }
}
