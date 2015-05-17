/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.DbAgentFileOwner;
import java.util.List;
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
    List<DbAgentFileOwner> findByName(@Param("name") String name);
}
