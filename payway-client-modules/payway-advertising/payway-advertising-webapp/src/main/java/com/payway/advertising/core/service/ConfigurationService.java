/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.model.DbConfiguration;
import com.payway.advertising.model.DbUser;
import java.util.List;

/**
 * ConfigurationService
 *
 * @author Sergey Kichenko
 * @created 15.05.15 00:00
 */
public interface ConfigurationService extends CrudEntityService<Long, DbConfiguration> {

    DbConfiguration findConfigurationByUser(DbUser user, boolean isCreate) throws ServiceException;

    DbConfiguration findConfigurationByNameWithFiles(String name) throws ServiceException;

    List<FileSystemObject> files(FileSystemObject localConfigPath) throws ServiceException;

    void copy(FileSystemObject src, FileSystemObject dst) throws ServiceException;

    void remove(FileSystemObject src) throws ServiceException;

    void rename(FileSystemObject src, FileSystemObject dst) throws ServiceException;

    String generateUniqueFolderName(String prefix, String folder);
}
