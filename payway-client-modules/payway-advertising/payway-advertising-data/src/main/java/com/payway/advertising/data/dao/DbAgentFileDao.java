/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.DbAgentFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * DbAgentFileDao
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface DbAgentFileDao extends JpaRepository<DbAgentFile, Long> {

    @Query(value = "select o from DbAgentFile o where o.name like :name%")
    List<DbAgentFile> findStartWithByName(@Param("name") String name);

    @Query(value = "select o from DbAgentFile o where o.name in :names")
    List<DbAgentFile> findAllByName(@Param("names") List<String> names);
}
