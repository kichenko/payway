/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.dao;

import com.payway.advertising.model.common.DbConfiguration;
import com.payway.advertising.model.common.DbConfigurationKeyType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * ConfigurationDao
 *
 * @author Sergey Kichenko
 * @created 29.06.15 00:00
 */
public interface ConfigurationDao extends JpaRepository<DbConfiguration, Long> {

    @Query(value = "select c from DbConfiguration c where c.key in (:keys)")
    List<DbConfiguration> getByKeys(@Param("keys") List<DbConfigurationKeyType> keys);

    @Query(value = "select c from DbConfiguration c where c.key = :key")
    DbConfiguration getByKey(@Param("keys") DbConfigurationKeyType key);

    @Modifying
    @Query(value = "update DbConfiguration c set value=:value where c.key = :key")
    int updateByKey(@Param("key") DbConfigurationKeyType key, @Param("value") String value);
}
