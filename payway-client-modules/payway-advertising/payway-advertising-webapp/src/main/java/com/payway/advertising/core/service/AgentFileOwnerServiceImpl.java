/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.AgentFileOwnerDao;
import com.payway.advertising.model.DbAgentFileOwner;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    //### bad count query no need
    @Override
    @Transactional(readOnly = true)
    public List<DbAgentFileOwner> findByName(String name, int start, int size, Sort sort) throws ServiceException {

        Page<DbAgentFileOwner> page = agentFileOwnerDao.findByName(name, new PageRequest(start / size, size, sort));
        if (!page.hasContent()) {
            return Collections.<DbAgentFileOwner>emptyList();
        }

        return page.getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public long countFindByName(String name) throws ServiceException {
        return agentFileOwnerDao.countFindByName(name);
    }

    //### bad count query no need
    @Override
    @Transactional(readOnly = true)
    public List<DbAgentFileOwner> list(int start, int size, Sort sort) throws ServiceException {

        Page<DbAgentFileOwner> page = agentFileOwnerDao.list(new PageRequest(start / size, size, sort));
        if (!page.hasContent()) {
            return Collections.<DbAgentFileOwner>emptyList();
        }

        return page.getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public long countList() throws ServiceException {
        return agentFileOwnerDao.countList();
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
}
