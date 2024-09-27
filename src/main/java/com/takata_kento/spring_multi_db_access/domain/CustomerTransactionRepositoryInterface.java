package com.takata_kento.spring_multi_db_access.domain;

public interface CustomerTransactionRepositoryInterface {
    Transaction getTransactionInfoById(int transactionId);
    Customer getCustomerInfoById(int customerId);
}
