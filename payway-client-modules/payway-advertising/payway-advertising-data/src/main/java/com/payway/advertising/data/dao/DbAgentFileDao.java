/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.DbAgentFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DbAgentFileDao
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface DbAgentFileDao extends JpaRepository<DbAgentFile, Long> {

}
