package com.takata_kento.spring_multi_db_access.infrastructure;

import com.takata_kento.spring_multi_db_access.annotation.CustomerDataJdbcClient;
import com.takata_kento.spring_multi_db_access.annotation.TransactionDataJdbcClient;
import com.takata_kento.spring_multi_db_access.domain.Customer;
import com.takata_kento.spring_multi_db_access.domain.CustomerTransactionRepositoryInterface;
import com.takata_kento.spring_multi_db_access.domain.Transaction;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerTransactionRepository implements CustomerTransactionRepositoryInterface {
    private final JdbcClient transactionDataJdbcClient;
    private final JdbcClient customerDataJdbcClient;

    public CustomerTransactionRepository(
            @TransactionDataJdbcClient JdbcClient transactionDataJdbcClient,
            @CustomerDataJdbcClient    JdbcClient customerDataJdbcClient
    ) {
        this.transactionDataJdbcClient = transactionDataJdbcClient;
        this.customerDataJdbcClient = customerDataJdbcClient;
    }

    public Transaction getTransactionInfoById(int transactionId) {
        return this.transactionDataJdbcClient
                .sql("""
                    SELECT
                      log.transaction_id, log.customer_id, log.total, log.date
                    FROM
                      db1testdata.transaction_log as log
                    WHERE
                      log.transaction_id = :transaction_id
                """)
                .param("transaction_id", transactionId)
                .query(
                    (rs, rowNum) -> new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("total"),
                        rs.getDate("date"))
                )
                .single();
    }

    public Customer getCustomerInfoById(int customerId) {
        return this.customerDataJdbcClient
                .sql("""
                    SELECT
                      customer.customer_id, customer.name, customer.age, customer.birthday, customer.gender
                    FROM
                      db2testdata.customer_info as customer
                    WHERE
                      customer.customer_id = :customer_id
                """)
                .param("customer_id", customerId)
                .query(
                    (rs, rowNum) -> new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getDate("birthday"),
                        rs.getString("gender"))
                )
                .single();
    }
}
