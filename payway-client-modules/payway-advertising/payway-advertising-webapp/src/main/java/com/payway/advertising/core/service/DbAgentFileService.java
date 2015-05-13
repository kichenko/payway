/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.core.service;

import com.payway.advertising.model.DbAgentFile;
import java.util.List;

/**
 * DbAgentFileService
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface DbAgentFileService extends CrudEntityService<Long, DbAgentFile> {

    List<DbAgentFile> findAllByName(List<String> names);
    List<DbAgentFile> findStartWithByName(String name);
}
