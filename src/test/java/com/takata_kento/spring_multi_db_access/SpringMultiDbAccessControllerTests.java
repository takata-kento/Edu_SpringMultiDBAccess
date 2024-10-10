package com.takata_kento.spring_multi_db_access;

import com.takata_kento.spring_multi_db_access.annotation.CustomerDataJdbcClient;
import com.takata_kento.spring_multi_db_access.annotation.TransactionDataJdbcClient;
import com.takata_kento.spring_multi_db_access.infrastructure.CustomerTransactionRepository;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class SpringMultiDbAccessControllerTests {
    @Autowired
    MockMvc mockMvc;

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
    @TransactionDataJdbcClient
    JdbcClient transactionDataJdbcClient;

    @Autowired
    @CustomerDataJdbcClient
    JdbcClient customerDataJdbcClient;

    @Autowired
    CustomerTransactionRepository customerTransactionRepository;

    @Sql(scripts = {"/transactionDbInit.sql"},
            config  = @SqlConfig(dataSource = "transactionDataSource")
    )

    @Test
    void shouldReturnMessage() throws Exception {
        // Given
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

        String expect =
                """
                    {
                        "transactionId": 24042101,
                        "customerId": 1,
                        "total": 400,
                        "date": "2024-04-21"
                    }
                """;

        // When
        String actual = mockMvc.perform(MockMvcRequestBuilders.get("/getTransactionInfo/24042101"))
                               .andDo(MockMvcResultHandlers.print())
                               .andExpect(MockMvcResultMatchers.status().isOk())
                               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        // Then
        JSONAssert.assertEquals(expect, actual, JSONCompareMode.STRICT);
    }
}
