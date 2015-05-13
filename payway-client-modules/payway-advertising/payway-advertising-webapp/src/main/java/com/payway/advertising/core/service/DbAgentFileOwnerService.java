/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.model.DbAgentFileOwner;
import java.util.List;

/**
 * DbAgentFileOwnerService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface DbAgentFileOwnerService extends CrudEntityService<Long, DbAgentFileOwner> {

    List<DbAgentFileOwner> findByName(String name) throws ServiceException;
}
