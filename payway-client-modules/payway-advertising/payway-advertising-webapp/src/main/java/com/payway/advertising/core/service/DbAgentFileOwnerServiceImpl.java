/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.data.dao.DbAgentFileOwnerDao;
import com.payway.advertising.model.DbAgentFileOwner;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DbAgentFileOwnerServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
@Service(value = "dbAgentFileOwnerService")
public class DbAgentFileOwnerServiceImpl implements DbAgentFileOwnerService {

    @Autowired
    private DbAgentFileOwnerDao dbAgentFileOwnerDao;

    @Override
    @Transactional(readOnly = true)
    public List<DbAgentFileOwner> findByName(String name) throws ServiceException {
        return dbAgentFileOwnerDao.findByName(name);
    }

    @Override
    public DbAgentFileOwner insert(DbAgentFileOwner entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DbAgentFileOwner update(DbAgentFileOwner entity) throws ServiceException {
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
        return dbAgentFileOwnerDao.findAll();
    }
}
