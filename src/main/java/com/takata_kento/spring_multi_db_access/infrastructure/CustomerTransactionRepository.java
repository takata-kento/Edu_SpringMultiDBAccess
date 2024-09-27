package com.takata_kento.spring_multi_db_access.infrastructure;

import com.takata_kento.spring_multi_db_access.domain.Customer;
import com.takata_kento.spring_multi_db_access.domain.CustomerTransactionRepositoryInterface;
import com.takata_kento.spring_multi_db_access.domain.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerTransactionRepository implements CustomerTransactionRepositoryInterface {
    private final JdbcTemplate transactionDataJdbcTemplate;
    private final JdbcTemplate customerDataJdbcTemplate;

    public CustomerTransactionRepository(
            @Qualifier("transactionDataJdbcTemplate") JdbcTemplate transactionDataJdbcTemplate,
            @Qualifier("customerDataJdbcTemplate")    JdbcTemplate customerDataJdbcTemplate
    ) {
        this.transactionDataJdbcTemplate = transactionDataJdbcTemplate;
        this.customerDataJdbcTemplate = customerDataJdbcTemplate;
    }

    public Transaction getTransactionInfoById(String transactionId) {
        return this.transactionDataJdbcTemplate.queryForObject(
                """
                    SELECT
                      log.transaction_id, log.customer_id, log.total, log.date
                    FROM
                      db1testdata.transaction_log as log
                    WHERE
                      log.transaction_id = ?
                """,
                (rs, rowNum) -> new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("total"),
                        rs.getDate("date")
                ),
                transactionId
        );
    }

    public Customer getCustomerInfoById(String customerId) {
        return this.customerDataJdbcTemplate.queryForObject(
                """
                    SELECT
                      customer.customer_id, customer.name, customer.age, customer.birthday, customer.gender
                    FROM
                      db2testdata.customer_info as customer
                    WHERE
                      customer.customer_id = ?
                """,
                (rs, rowNum) -> new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getDate("birthday"),
                        rs.getString("gender")
                ),
                customerId
        );
    }
}
