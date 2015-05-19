/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.DbAgentFileOwner;
import java.util.Set;

/**
 * AgentFileOwnerService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface AgentFileOwnerService extends CrudEntityService<Long, DbAgentFileOwner> {

    Set<DbAgentFileOwner> findByName(String name) throws ServiceException;
}
