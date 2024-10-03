package com.takata_kento.spring_multi_db_access.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
public class MultiDbAccessDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("app.datasource.transaction")
    public DataSourceProperties transactionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.transaction.configuration")
    public HikariDataSource transactionDataSource(
            @Qualifier("transactionDataSourceProperties") DataSourceProperties transactionDataSourceProperties
    ) {
        return transactionDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    JdbcClient transactionDataJdbcClient(
            @Qualifier("transactionDataSource")DataSource dataSource
    ) {
        return JdbcClient.create(dataSource);
    }

    @Bean
    @ConfigurationProperties("app.datasource.customer")
    public DataSourceProperties customerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.customer.configuration")
    public HikariDataSource customerDataSource(
            @Qualifier("customerDataSourceProperties") DataSourceProperties customerDataSourceProperties
    ) {
        return customerDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    JdbcClient customerDataJdbcClient(
            @Qualifier("customerDataSource")DataSource dataSource
    ) {
        return JdbcClient.create(dataSource);
    }
}
