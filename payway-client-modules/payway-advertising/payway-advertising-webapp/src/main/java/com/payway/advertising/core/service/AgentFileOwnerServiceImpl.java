/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.AgentFileOwnerDao;
import com.payway.advertising.model.DbAgentFileOwner;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AgentFileOwnerServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
@Service(value = "agentFileOwnerService")
public class AgentFileOwnerServiceImpl implements AgentFileOwnerService {

    @Autowired
    private AgentFileOwnerDao agentFileOwnerDao;

    @Override
    @Transactional(readOnly = true)
    public Set<DbAgentFileOwner> findByName(String name) throws ServiceException {
        return agentFileOwnerDao.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DbAgentFileOwner> findByName(String name, Pageable pageable) throws ServiceException {
        return agentFileOwnerDao.findByName(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DbAgentFileOwner> list(Pageable pageable) throws ServiceException {
        return agentFileOwnerDao.list(pageable);
    }

    @Override
    public DbAgentFileOwner save(DbAgentFileOwner entity) throws ServiceException {
        return agentFileOwnerDao.save(entity);
    }

    @Override
    public void delete(DbAgentFileOwner entity) throws ServiceException {
        agentFileOwnerDao.delete(entity);
    }

    @Override
    public DbAgentFileOwner getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
