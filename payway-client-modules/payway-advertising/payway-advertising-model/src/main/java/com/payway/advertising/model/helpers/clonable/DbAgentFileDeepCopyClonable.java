/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.model.helpers.clonable;

import com.payway.advertising.model.DbAgentFile;

/**
 * DbAgentFileDeepCopyClonable
 *
 * @author Sergey Kichenko
 * @created 21.05.15 00:00
 */
public class DbAgentFileDeepCopyClonable implements EntityClonable<DbAgentFile> {

    @Override
    public DbAgentFile clone(DbAgentFile file) {
        return new DbAgentFile(file.getId(), file.getName(), file.getKind(), file.getOwner() != null ? new DbAgentFileOwnerDeepCopyClonable().clone(file.getOwner()) : null, file.getExpression(), file.getDigest(), file.getIsCountHits(), file.getConfiguration() != null ? new DbConfigurationDeepCopyClonable().clone(file.getConfiguration()) : null, file.getSeqNo());
    }
}
