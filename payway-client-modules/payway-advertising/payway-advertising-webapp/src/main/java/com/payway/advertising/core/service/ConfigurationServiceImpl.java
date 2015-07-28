/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.ConfigurationDao;
import com.payway.advertising.model.common.DbConfiguration;
import com.payway.advertising.model.common.DbConfigurationKeyType;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ConfigurationServiceImpl
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
@Service(value = "app.advertising.ConfigurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void save(List<DbConfiguration> configs) throws ServiceException {

        for (DbConfiguration config : configs) {
            if (configurationDao.updateByKey(config.getKey(), config.getValue()) == 0) {
                configurationDao.save(config);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public DbConfiguration getByKey(DbConfigurationKeyType key) throws ServiceException {
        return configurationDao.getByKey(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DbConfiguration> getByKeys(List<DbConfigurationKeyType> keys) throws ServiceException {
        return configurationDao.getByKeys(keys);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public DbConfiguration save(DbConfiguration entity) throws ServiceException {

        if (configurationDao.updateByKey(entity.getKey(), entity.getValue()) == 0) {
            return configurationDao.save(entity);
        }

        return entity;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(DbConfiguration entity) throws ServiceException {
        configurationDao.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public DbConfiguration getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
