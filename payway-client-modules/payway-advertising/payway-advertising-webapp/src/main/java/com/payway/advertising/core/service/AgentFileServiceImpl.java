/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.core.service.file.FileSystemManagerService;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.data.dao.AgentFileDao;
import com.payway.advertising.model.DbAgentFile;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AgentFileServiceImpl
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
@Service(value = "app.advertising.AgentFileService")
public class AgentFileServiceImpl implements AgentFileService {

    @Autowired
    private AgentFileDao agentFileDao;

    @Autowired
    private FileSystemManagerService fileSystemManagerService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateByNamePrefix(String srcName, String dstName, FileSystemObject foOld, FileSystemObject foNew) throws ServiceException {
        agentFileDao.updateByNamePrefix(srcName, dstName);
        fileSystemManagerService.rename(foOld, foNew);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteByNamePrefix(String srcName, FileSystemObject fo) throws ServiceException {
        agentFileDao.deleteByNamePrefix(srcName);
        fileSystemManagerService.delete(fo);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<DbAgentFile> findAllByName(Set<String> names) throws ServiceException {
        if (names != null && !names.isEmpty()) {
            return agentFileDao.findAllByName(names);
        }
        return Collections.emptySet();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<DbAgentFile> findStartWithByName(String name) throws ServiceException {
        return agentFileDao.findStartWithByName(name);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public DbAgentFile save(DbAgentFile entity) throws ServiceException {
        return agentFileDao.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DbAgentFile> findAll(Sort sort) throws ServiceException {
        return agentFileDao.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
    }

    @Override
    public void delete(DbAgentFile entity) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DbAgentFile getById(Long id) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
