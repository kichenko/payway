/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.DbConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * ConfigurationDao
 *
 * @author Sergey Kichenko
 * @created 15.05.15 00:00
 */
public interface ConfigurationDao extends JpaRepository<DbConfiguration, Long> {

    @Query(value = "select c from DbConfiguration c where c.name = :name")
    DbConfiguration findByName(@Param("name") String name);

    @Query(value = "select c from DbConfiguration c left join fetch c.files f where c.name = :name")
    DbConfiguration findByNameWithFiles(@Param("name") String name);
}
