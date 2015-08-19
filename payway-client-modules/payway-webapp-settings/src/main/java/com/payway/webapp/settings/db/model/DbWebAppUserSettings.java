/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.db.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DbWebAppUserSettings
 *
 * @author Sergey Kichenko
 * @created 17.08.2015
 */
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DbWebAppUserSettings implements Serializable {

    private static final long serialVersionUID = -3308253383908941639L;

    private Long id;
    private String webAppId;
    private String login;
    private String key;
    private String value;

    public DbWebAppUserSettings(String webAppId, String login, String key, String value) {
        this.webAppId = webAppId;
        this.login = login;
        this.key = key;
        this.value = value;
    }

}
