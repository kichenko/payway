/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.AgentFileOwnerDao;
import com.payway.advertising.model.DbAgentFileOwner;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<DbAgentFileOwner> findByName(String name) throws ServiceException {
        return agentFileOwnerDao.findByName(name);
    }

    @Override
    public DbAgentFileOwner save(DbAgentFileOwner entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(DbAgentFileOwner entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DbAgentFileOwner getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<DbAgentFileOwner> list() throws ServiceException {
        return agentFileOwnerDao.findAll();
    }
}
