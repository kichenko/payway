/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.DbAgentFile;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * AgentFileDao
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface AgentFileDao extends JpaRepository<DbAgentFile, Long> {

    @Query(value = "select f from DbAgentFile f where f.name like :name || '%'")
    Set<DbAgentFile> findStartWithByName(@Param("name") String name);

    @Query(value = "select f from DbAgentFile f where f.name in :names")
    Set<DbAgentFile> findAllByName(@Param("names") Set<String> names);

    @Query(value = "select f from DbAgentFile f where f.name = :name")
    DbAgentFile findByName(@Param("name") String name);

    @Modifying
    @Query(value = "update DbAgentFile f set f.name = concat(:dstName, substring(f.name, length(:srcName)+1)) where f.name like :srcName || '%'")
    int updateByNamePrefix(@Param("srcName") String srcName, @Param("dstName") String dstName);

    @Modifying
    @Query(value = "delete from DbAgentFile f where f.name like :srcName || '%'")
    int deleteByNamePrefix(@Param("srcName") String srcName);

    @Query(value = "select coalesce(max(f.seqNo),0) from DbAgentFile f")
    int getNextSeqNo();

}
