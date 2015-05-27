/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.DbAgentFileOwner;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * AgentFileOwnerService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface AgentFileOwnerService extends CrudEntityService<Long, DbAgentFileOwner> {

    Set<DbAgentFileOwner> findByName(String name) throws ServiceException;

    Page<DbAgentFileOwner> findByName(String name, Pageable pageable) throws ServiceException;

    Page<DbAgentFileOwner> list(Pageable pageable) throws ServiceException;
}
