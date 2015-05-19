/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.data.dao.ConfigurationDao;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ConfigurationServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Slf4j
@Service(value = "configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    @Transactional(readOnly = true)
    public DbConfiguration findConfigurationByUser(DbUser user, boolean isCreate) throws ServiceException {
        DbConfiguration config = configurationDao.findByName(user.getLogin());
        if (config == null && isCreate) {
            config = new DbConfiguration(user.getLogin(), user);
            configurationDao.save(config);
        }
        return config;
    }

    @Override
    @Transactional(readOnly = true)
    public DbConfiguration findConfigurationByNameWithFiles(String name) throws ServiceException {
        return configurationDao.findByNameWithFiles(name);
    }

    @Override
    public String generateUniqueFolderName(String prefix, String folder) {
        return String.format("%s_%s_%s", prefix, folder, UUID.randomUUID().toString());
    }

    @Override
    public DbConfiguration save(DbConfiguration entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(DbConfiguration entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DbConfiguration getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
