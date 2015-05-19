/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.DbAgentFile;
import java.util.Set;

/**
 * AgentFileService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface AgentFileService extends CrudEntityService<Long, DbAgentFile> {

    Set<DbAgentFile> findAllByName(Set<String> names) throws ServiceException;

    Set<DbAgentFile> findStartWithByName(String name) throws ServiceException;

    long updateByNamePrefix(String srcName, String dstName);

    long deleteByNamePrefix(String srcName);
}
