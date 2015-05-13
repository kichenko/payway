/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.data.dao.DbAgentFileDao;
import com.payway.advertising.model.DbAgentFile;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * DbAgentFileServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Service(value = "dbAgentFileService")
public class DbAgentFileServiceImpl implements DbAgentFileService {

    private DbAgentFileDao dbAgentFileDao;

    @Override
    public DbAgentFile insert(DbAgentFile entity) throws ServiceException {
        return dbAgentFileDao.save(entity);
    }

    @Override
    public DbAgentFile update(DbAgentFile entity) throws ServiceException {
        return dbAgentFileDao.save(entity);
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
