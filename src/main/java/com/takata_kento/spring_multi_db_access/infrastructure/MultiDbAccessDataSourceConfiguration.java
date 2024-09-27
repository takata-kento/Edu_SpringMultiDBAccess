package com.takata_kento.spring_multi_db_access.infrastructure;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration(proxyBeanMethods = false)
public class MultiDbAccessDataSourceConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.transaction.configuration")
    public DataSourceProperties transactionDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.transaction.configuration")
    public HikariDataSource transactionDataSource(DataSourceProperties transactionDataSourceProperties) {
        return transactionDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties("app.datasource.customer")
    public DataSourceProperties customerDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource.customer.configuration")
    public HikariDataSource customerDataSource(DataSourceProperties customerDataSourceProperties) {
        return customerDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }
}
