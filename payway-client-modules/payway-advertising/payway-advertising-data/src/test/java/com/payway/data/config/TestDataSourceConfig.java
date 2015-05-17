/*
 * (c) Payway, 2015. All right reserved.
 */
package com.payway.data.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Test datasource configuration
 *
 * @author Sergey Kichenko
 * @created 04.05.15 00:00
 */
@Configuration
public class TestDataSourceConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(org.postgresql.Driver.class.getName());
        dataSource.setUrl(env.getProperty("database.url"));
        dataSource.setUsername(env.getProperty("database.username"));
        dataSource.setPassword(env.getProperty("database.password"));

        return dataSource;
    }
}
