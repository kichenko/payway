/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.core.service.exception.ServiceException;
import com.payway.advertising.core.service.file.FileSystemObject;
import com.payway.advertising.model.DbAgentFile;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Sort;

/**
 * AgentFileService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface AgentFileService extends CrudEntityService<Long, DbAgentFile> {

    Set<DbAgentFile> findAllByName(Set<String> names) throws ServiceException;

    Set<DbAgentFile> findStartWithByName(String name) throws ServiceException;

    void updateByNamePrefix(String srcName, String dstName, FileSystemObject foOld, FileSystemObject foNew) throws ServiceException;

    void deleteByNamePrefix(String srcName, FileSystemObject fo) throws ServiceException;

    List<DbAgentFile> findAll(Sort sort) throws ServiceException;

    void saveAll(List<DbAgentFile> list);

    int getNextSeqNo();
}
