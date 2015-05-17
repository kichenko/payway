/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;

/**
 * ConfigurationService
 *
 * @author Sergey Kichenko
 * @created 15.05.15 00:00
 */
public interface ConfigurationService extends CrudEntityService<Long, DbConfiguration> {

    DbConfiguration findConfigurationByUserLogin(DbUser user, boolean isCreate) throws ServiceException;
}
