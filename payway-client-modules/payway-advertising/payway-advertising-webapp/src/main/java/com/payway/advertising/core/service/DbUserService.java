/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.DbUser;

/**
 * DbUserService
 *
 * @author Sergey Kichenko
 * @created 15.05.15 00:00
 */
public interface DbUserService extends CrudEntityService<Long, DbUser> {

    DbUser findUserByLogin(String login, boolean isCreate) throws ServiceException;
}
