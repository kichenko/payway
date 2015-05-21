/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model.helpers.clonable;

import com.payway.advertising.model.DbAgentFileOwner;

/**
 * DbAgentFileOwnerDeepCopyClonable
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public class DbAgentFileOwnerDeepCopyClonable implements EntityClonable<DbAgentFileOwner> {

    @Override
    public DbAgentFileOwner clone(DbAgentFileOwner owner) {
        return new DbAgentFileOwner(owner.getId(), owner.getName(), owner.getDescription());
    }
}
