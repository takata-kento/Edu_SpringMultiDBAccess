package com.takata_kento.spring_multi_db_access.infrastructure;

import com.takata_kento.spring_multi_db_access.annotation.*;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
public class MultiDbAccessDataSourceConfiguration {
    @Bean
    @ConfigurationProperties("app.datasource.transaction")
    @TransactionDataSourceProperties
    public DataSourceProperties transactionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.transaction.configuration")
    @TransactionDataSource
    public HikariDataSource transactionDataSource(
            @TransactionDataSourceProperties DataSourceProperties transactionDataSourceProperties
    ) {
        return transactionDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @TransactionDataJdbcClient
    JdbcClient transactionDataJdbcClient(
            @TransactionDataSource DataSource dataSource
    ) {
        return JdbcClient.create(dataSource);
    }

    @Bean
    @ConfigurationProperties("app.datasource.customer")
    @CustomerDataSourceProperties
    public DataSourceProperties customerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.customer.configuration")
    @CustomerDataSource
    public HikariDataSource customerDataSource(
            @CustomerDataSourceProperties DataSourceProperties customerDataSourceProperties
    ) {
        return customerDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @CustomerDataJdbcClient
    JdbcClient customerDataJdbcClient(
            @CustomerDataSource DataSource dataSource
    ) {
        return JdbcClient.create(dataSource);
    }
}
