package com.takata_kento.spring_multi_db_access;

import com.takata_kento.spring_multi_db_access.domain.Customer;
import com.takata_kento.spring_multi_db_access.domain.Transaction;
import com.takata_kento.spring_multi_db_access.infrastructure.CustomerTransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Date;

@SpringBootTest
@Testcontainers
class SpringMultiDbAccessApplicationTests {
    @Container
    static PostgreSQLContainer<?> transactionDBContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.4"))
                    .withUsername("test")
                    .withPassword("P@ssw0rd!")
                    .withDatabaseName("transactionDb");

    @Container
    static PostgreSQLContainer<?> customerDBContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.4"))
                    .withUsername("test")
                    .withPassword("P@ssw0rd!")
                    .withDatabaseName("customerDb");

    @DynamicPropertySource
    static void setTestProperty(DynamicPropertyRegistry registry) {
        registry.add("app.datasource.transaction.url", transactionDBContainer::getJdbcUrl);
        registry.add("app.datasource.transaction.username", transactionDBContainer::getUsername);
        registry.add("app.datasource.transaction.password", transactionDBContainer::getPassword);
        registry.add("app.datasource.customer.url", customerDBContainer::getJdbcUrl);
        registry.add("app.datasource.customer.username", customerDBContainer::getUsername);
        registry.add("app.datasource.customer.password", customerDBContainer::getPassword);
    }

    @Autowired
    @Qualifier("transactionDataJdbcClient")
    JdbcClient transactionDataJdbcClient;

    @Autowired
    @Qualifier("customerDataJdbcClient")
    JdbcClient customerDataJdbcClient;

    @Autowired
    CustomerTransactionRepository customerTransactionRepository;

    @Test
    void repositoryTransactionDataTest() {
        // Given
        transactionDataJdbcClient
                .sql("""
                    CREATE SCHEMA db1testdata;
                """)
                .update();

        transactionDataJdbcClient
                .sql("""
                    CREATE TABLE db1testdata.transaction_log (
                        transaction_id integer,
                        customer_id    integer,
                        total          integer,
                        date           timestamptz
                    );
                """)
                .update();

        transactionDataJdbcClient
                .sql("""
                    INSERT INTO
                      db1testdata.transaction_log
                        (transaction_id, customer_id     , total  , date)
                    VALUES
                        ('24042101'    , '1'             , '400'  , '2024-04-21 10:23:54+09:00'),
                        ('24071101'    , '3'             , '20010', '2024-07-11 11:03:20+09:00'),
                        ('24060101'    , '2'             , '9541' , '2024-06-01 01:41:42+09:00'),
                        ('24102101'    , '4'             , '50021', '2024-10-21 20:38:02+09:00');
                """)
                .update();
        Transaction expectedData = new Transaction(24042101, 1, 400, Date.valueOf("2024-04-21"));

        // When
        Transaction actualData = this.customerTransactionRepository.getTransactionInfoById(24042101);

        // Then
        Assertions.assertEquals(expectedData.transactionId(), actualData.transactionId());
        Assertions.assertEquals(expectedData.customerId(), actualData.customerId());
        Assertions.assertEquals(expectedData.total(), actualData.total());
        Assertions.assertEquals(expectedData.date(), actualData.date());
    }

    @Test
    void customerDataJdbcClientTest() {
        // Given
        customerDataJdbcClient
                .sql("""
                    CREATE SCHEMA db2testdata;
                """)
                .update();

        customerDataJdbcClient
                .sql("""
                    CREATE TABLE db2testdata.customer_info (
                        customer_id integer,
                        name        character varying(50),
                        age         integer,
                        birthday    date,
                        gender      character varying(1)
                    );
                """)
                .update();

        customerDataJdbcClient
                .sql("""
                    INSERT INTO
                      db2testdata.customer_info
                        (customer_id, name     , age , birthday    , gender)
                    VALUES
                        ('1'        , 'alice'  , '10', '2014-03-31', '1'),
                        ('2'        , 'bob'    , '27', '1997-08-13', '0'),
                        ('3'        , 'charlie', '72', '1952-11-04', '1'),
                        ('4'        , 'dave'   , '47', '1977-12-14', '0');
                """)
                .update();
        Customer expectedData = new Customer(1, "alice", 10, Date.valueOf("2014-03-31"), "1");

        // When
        Customer actualData = this.customerTransactionRepository.getCustomerInfoById(1);

        // Then
        Assertions.assertEquals(expectedData.customerId(), actualData.customerId());
        Assertions.assertEquals(expectedData.name(), actualData.name());
        Assertions.assertEquals(expectedData.age(), actualData.age());
        Assertions.assertEquals(expectedData.birthday(), actualData.birthday());
        Assertions.assertEquals(expectedData.gender(), actualData.gender());
    }
}
