/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.AgentFileDao;
import com.payway.advertising.model.DbAgentFile;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AgentFileServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Service(value = "agentFileService")
public class AgentFileServiceImpl implements AgentFileService {

    @Autowired
    private AgentFileDao agentFileDao;

    @Override
    @Transactional
    public long updateByNamePrefix(String srcName, String dstName) {
        return agentFileDao.updateByNamePrefix(srcName, dstName);
    }

    @Override
    @Transactional
    public long deleteByNamePrefix(String srcName) {
        return agentFileDao.deleteByNamePrefix(srcName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DbAgentFile> findAllByName(List<String> names) throws ServiceException {
        if (names != null && !names.isEmpty()) {
            return agentFileDao.findAllByName(names);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DbAgentFile> findStartWithByName(String name) throws ServiceException {
        return agentFileDao.findStartWithByName(name);
    }

    @Override
    public DbAgentFile save(DbAgentFile entity) throws ServiceException {
        return agentFileDao.save(entity);
    }

    @Override
    public void delete(DbAgentFile entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DbAgentFile getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DbAgentFile> list() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
