/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.common.DbConfiguration;
import com.payway.advertising.model.common.DbConfigurationKeyType;
import java.util.List;

/**
 * AgentFileService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface ConfigurationService extends CrudEntityService<Long, DbConfiguration> {

    DbConfiguration getByKey(DbConfigurationKeyType key) throws ServiceException;

    List<DbConfiguration> getByKeys(List<DbConfigurationKeyType> keys) throws ServiceException;

    void save(List<DbConfiguration> configs) throws ServiceException;
}
