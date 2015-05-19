/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.data.dao.ConfigurationDao;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier(value = "fileManagerService")
    FileSystemManagerService fileManagerService;

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

    @Override
    public List<DbConfiguration> list() throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String generateUniqueFolderName(String prefix, String folder) {
        return String.format("%s_%s_%s", prefix, folder, UUID.randomUUID().toString());
    }

    @Override
    public List<FileSystemObject> files(FileSystemObject localConfigPath) throws ServiceException {
        return fileManagerService.list(localConfigPath, false, true);
    }

    @Override
    public void copy(FileSystemObject src, FileSystemObject dst) throws ServiceException {
        fileManagerService.copy(src, dst);
    }

    @Override
    public void remove(FileSystemObject src) throws ServiceException {
        fileManagerService.delete(src);
    }

    @Override
    public void rename(FileSystemObject src, FileSystemObject dst) throws ServiceException {
        fileManagerService.rename(src, dst);
    }
}
