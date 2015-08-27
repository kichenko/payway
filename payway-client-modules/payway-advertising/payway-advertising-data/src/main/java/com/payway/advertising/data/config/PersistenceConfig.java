/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.advertising.data.config;

import com.googlecode.flyway.core.Flyway;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
//TODO: webapp settings com.payway.webapp.settings.repository
@EnableJpaRepositories(basePackages = {"com.payway.advertising.data.dao", "com.payway.webapp.settings.repository"})
public class PersistenceConfig {

    /**
     * Datasource configured in other file
     */
    @Autowired
    private DataSource dataSource;

    @Value("${app.hibernate.show.sql:true}")
    private String appHibernateShowSql;

    @Value("${app.hibernate.format.sql:true}")
    private String appHibernateFormatSql;

    @Value("${app.hibernate.use.sql.comments:true}")
    private String appHibernateUseSqlComments;

    @Value("${app.hibernate.statement.fetch.size:50}")
    private String appHibernateStatementFetchSize;

    @Value("${app.hibernate.max.fetch.depth:3}")
    private String appHibernateMaxFetchDepth;

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setPersistenceUnitName("payway-advertising-data");
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factoryBean.setDataSource(dataSource);

        factoryBean.setMappingResources("DbAgentFile.hbm.xml", "DbAgentFileOwner.hbm.xml", "DbConfiguration.hbm.xml");
        //TODO: webapp settings 
        factoryBean.setPackagesToScan("com.payway.webapp.settings.db.model");

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty(AvailableSettings.DIALECT, PostgreSQL82Dialect.class.getName());
        jpaProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, "validate");
        jpaProperties.setProperty(AvailableSettings.RELEASE_CONNECTIONS, "after_transaction");
        jpaProperties.setProperty(AvailableSettings.SHOW_SQL, appHibernateShowSql);
        jpaProperties.setProperty(AvailableSettings.FORMAT_SQL, appHibernateFormatSql);
        jpaProperties.setProperty(AvailableSettings.USE_SQL_COMMENTS, appHibernateUseSqlComments);
        jpaProperties.setProperty(AvailableSettings.MAX_FETCH_DEPTH, appHibernateMaxFetchDepth);
        jpaProperties.setProperty(AvailableSettings.STATEMENT_FETCH_SIZE, appHibernateStatementFetchSize);
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
