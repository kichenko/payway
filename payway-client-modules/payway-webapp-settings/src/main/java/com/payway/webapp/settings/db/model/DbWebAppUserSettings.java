/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.webapp.settings.db.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Entity
@Table(name = "web_app_user_settings")
public class DbWebAppUserSettings implements Serializable {

    private static final long serialVersionUID = -3308253383908941639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String appId;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private String className;

    public DbWebAppUserSettings(String appId, String login, String className, String key, String value) {
        this.appId = appId;
        this.login = login;
        this.className = className;
        this.key = key;
        this.value = value;
    }
}
