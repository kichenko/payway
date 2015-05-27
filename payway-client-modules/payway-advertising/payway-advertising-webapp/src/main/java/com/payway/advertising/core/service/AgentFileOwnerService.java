/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.model.DbAgentFileOwner;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;

/**
 * AgentFileOwnerService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface AgentFileOwnerService extends CrudEntityService<Long, DbAgentFileOwner> {

    Set<DbAgentFileOwner> findByName(String name) throws ServiceException;

    List<DbAgentFileOwner> findByName(String name, int start, int size, Sort sort) throws ServiceException;

    List<DbAgentFileOwner> list(int start, int size, Sort sort) throws ServiceException;

    long countFindByName(String name) throws ServiceException;

    long countList() throws ServiceException;

}
