/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.DbConfigurationDao;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DbConfigurationServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
@Service(value = "dbConfigurationService")
public class DbConfigurationServiceImpl implements DbConfigurationService {

    @Autowired
    private DbConfigurationDao dbConfigurationDao;

    @Override
    public DbConfiguration findConfigurationByUserLogin(DbUser user, boolean isCreate) throws ServiceException {
        DbConfiguration config = dbConfigurationDao.findByLogin(user.getLogin());
        if (config == null && isCreate) {
            config = new DbConfiguration(user.getLogin(), user);
            dbConfigurationDao.save(config);
        }
        return config;
    }

    @Override
    public DbConfiguration save(DbConfiguration entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(DbConfiguration entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DbConfiguration getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DbConfiguration> list() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
