package com.takata_kento.spring_multi_db_access.domain;

public interface CustomerTransactionRepositoryInterface {
    Transaction getTransactionInfoById(String transactionId);
    Customer getCustomerInfoById(String customerId);
}
