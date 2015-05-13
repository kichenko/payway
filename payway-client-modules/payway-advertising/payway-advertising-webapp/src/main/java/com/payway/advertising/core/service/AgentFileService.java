/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.data.dao.DbAgentFileDao;
import com.payway.advertising.model.DbAgentFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AgentFileService
 *
 * @author Sergey Kichenko
 * @created 05.05.15 00:00
 */
@Slf4j
@Service
public class AgentFileService {

    @Autowired
    private DbAgentFileDao dbAgentFileDao;

    @Transactional
    public DbAgentFile save(DbAgentFile entity) {
        return dbAgentFileDao.save(entity);
    }

    @Transactional
    public void delete(DbAgentFile entity) {
        dbAgentFileDao.delete(entity);
    }
}
