/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.repository;

import com.payway.webapp.settings.db.model.DbWebAppUserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DbWebAppUserSettingsDao
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
public interface WebAppUserSettingsRepository extends JpaRepository<DbWebAppUserSettings, Long> {

    DbWebAppUserSettings findByAppIdAndLoginAndKey(String appId, String login, String key);
}
