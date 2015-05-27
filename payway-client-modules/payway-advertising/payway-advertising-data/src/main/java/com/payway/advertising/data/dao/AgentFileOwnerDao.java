/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.DbAgentFileOwner;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * AgentFileOwnerDao
 *
 * @author Sergey Kichenko
 * @created 13.05.15 00:00
 */
public interface AgentFileOwnerDao extends JpaRepository<DbAgentFileOwner, Long> {

    @Query(value = "select o from DbAgentFileOwner o where o.name like '%' || :name || '%'")
    Set<DbAgentFileOwner> findByName(@Param("name") String name);

    @Query(value = "select o from DbAgentFileOwner o where o.name like '%' || :name || '%'")
    Page<DbAgentFileOwner> findByName(@Param("name") String name, Pageable pageable);

    @Query(value = "select o from DbAgentFileOwner o")
    Page<DbAgentFileOwner> list(Pageable pageable);

}
