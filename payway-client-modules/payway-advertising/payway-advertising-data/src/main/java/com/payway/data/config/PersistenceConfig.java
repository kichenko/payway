/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.data.config;

import com.googlecode.flyway.core.Flyway;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration connect to DB, transactions, ORM-mapping.
 *
 * @author Sergey Kichenko
 * @created 04.05.15 00:00
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.payway.advertising.data.dao")
public class PersistenceConfig {

    /**
     * Datasource configured in other file
     */
    @Autowired
    private DataSource dataSource;

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setPersistenceUnitName("payway-advertising-data");
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factoryBean.setDataSource(dataSource);

        factoryBean.setMappingResources("DbAgentFile.hbm.xml", "DbAgentFileOwner.hbm.xml");

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty(AvailableSettings.DIALECT, PostgreSQL82Dialect.class.getName());
        jpaProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, "validate");
        jpaProperties.setProperty(AvailableSettings.RELEASE_CONNECTIONS, "after_transaction");
        jpaProperties.setProperty(AvailableSettings.SHOW_SQL, "true");
        jpaProperties.setProperty(AvailableSettings.FORMAT_SQL, "true");
        jpaProperties.setProperty(AvailableSettings.USE_SQL_COMMENTS, "true");
        jpaProperties.setProperty(AvailableSettings.STATEMENT_FETCH_SIZE, "50");
        jpaProperties.setProperty(org.hibernate.jpa.AvailableSettings.NAMING_STRATEGY, ImprovedNamingStrategy.class.getName());
        jpaProperties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");

        factoryBean.setJpaProperties(jpaProperties);

        /**
         * Run migration on app start
         */
        Flyway flyway = new Flyway();

        flyway.setDataSource(dataSource);
        flyway.migrate();

        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }
}
