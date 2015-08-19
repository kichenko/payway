/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.repository;

import com.payway.webapp.settings.db.model.DbWebAppUserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * DbWebAppUserSettingsDao
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
public interface WebAppUserSettingsRepository extends JpaRepository<DbWebAppUserSettings, Long> {

    @Query(value = "select s from DbWebAppUserSettings s "
            + "where "
            + "lower(s.appId) = lower(:appId) and "
            + "lower(s.login) = lower(:login) and "
            + "lower(s.key) = lower(:key)"
    )
    DbWebAppUserSettings findByAppIdAndLoginAndKey(@Param("appId") String appId, @Param("login") String login, @Param("key") String key);
}
