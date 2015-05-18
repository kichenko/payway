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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
    public DbConfiguration findConfigurationByUserLogin(DbUser user, boolean isCreate) throws ServiceException {
        DbConfiguration config = configurationDao.findByLogin(user.getLogin());
        if (config == null && isCreate) {
            config = new DbConfiguration(user.getLogin(), user);
            configurationDao.save(config);
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

    public String generateUniqueFolderName(String prefix, String folder) {
        return String.format("%s_%s_%s", prefix, folder, UUID.randomUUID().toString());
    }

    /**
     * Lock server configuration (folder + db)
     *
     * @throws com.payway.advertising.core.service.exception.ServiceException
     */
    public void lock() throws ServiceException {
        //
    }

    /**
     * Unlock server configuration (folder + db)
     *
     * @throws com.payway.advertising.core.service.exception.ServiceException
     */
    public void unlock() throws ServiceException {
        //
    }

    /**
     * Check for lock server configuration (folder + db)
     *
     * @return
     * @throws com.payway.advertising.core.service.exception.ServiceException
     */
    public boolean checkLock() throws ServiceException {
        return false;
    }

    /**
     * Copy local to tmp remote folder (folder)
     *
     * @param localTmpRemoteFolderName
     * @param localConfigPath - c:/store/login
     * @param remoteRootPath - http://127.0.0.1/store
     * @param callback
     * @throws com.payway.advertising.core.service.exception.ServiceException
     */
    public void copyLocal2RemoteTemp(String localTmpRemoteFolderName, FileSystemObject localConfigPath, FileSystemObject remoteRootPath, FileCopyCallBack callback) throws ServiceException {
        try {
            if (FileSystemObject.FileType.FOLDER.equals(localConfigPath.getFileType()) && FileSystemObject.FileType.FOLDER.equals(remoteRootPath.getFileType())) {

                boolean cancel = false;

                //1. list local files
                List<FileSystemObject> files = fileManagerService.list(localConfigPath, false, true);

                //2. create tmp folder on remote host(webdav)
                fileManagerService.create(new FileSystemObject(remoteRootPath.getPath() + "/" + localTmpRemoteFolderName, remoteRootPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));

                //3. copy files to tmp folder on remote host(webdav)
                if (files != null) {
                    for (FileSystemObject f : files) {
                        fileManagerService.copy(f, new FileSystemObject(remoteRootPath.getPath() + "/" + localTmpRemoteFolderName + "/" + StringUtils.substringAfterLast(f.getPath(), "/"), remoteRootPath.getFileSystemType(), FileSystemObject.FileType.FILE, 0L, null));
                        if (callback != null) {
                            cancel = callback.copy(f);
                            if (cancel) {
                                break;
                            }
                        }
                    }

                    //3.1 remove tmp remote folder on 'cancel'
                    if (cancel) {
                        fileManagerService.delete(new FileSystemObject(remoteRootPath.getPath() + "/" + localTmpRemoteFolderName, remoteRootPath.getFileSystemType(), FileSystemObject.FileType.FOLDER, 0L, null));
                    }

                    //4. finish copy local to tmp remote
                    if (callback != null) {
                        callback.finish();
                    }
                }
            } else {
                throw new Exception("Local and/or remote path must be folder");
            }
        } catch (Exception ex) {
            log.error("", ex);
        }
    }

    /**
     * Rename remote to tmp (folder)
     *
     * @param remoteTmpFolderName
     * @param remotePath
     * @param remoteRootPath
     * @throws com.payway.advertising.core.service.exception.ServiceException
     */
    public void renameRemote2Temp(String remoteTmpFolderName, FileSystemObject remotePath, FileSystemObject remoteRootPath) throws ServiceException {
        fileManagerService.rename(new FileSystemObject(remoteRootPath.getPath() + "/" + remoteTmpFolderName, remotePath.getFileSystemType(), FileSystemObject.FileType.FILE, 0L, null), null);
    }

    /**
     * Rename remote tmp local to remote original (folder)
     *
     * @param remoteLocalTempPath
     * @param remotePath
     * @throws com.payway.advertising.core.service.exception.ServiceException
     */
    public void renameLocalRemoteTemp2Remote(FileSystemObject remoteLocalTempPath, FileSystemObject remotePath) throws ServiceException {
        fileManagerService.rename(remoteLocalTempPath, remotePath);
    }
}
