package com.takata_kento.spring_multi_db_access.application;

import com.takata_kento.spring_multi_db_access.domain.Customer;
import com.takata_kento.spring_multi_db_access.domain.CustomerTransactionRepositoryInterface;
import com.takata_kento.spring_multi_db_access.domain.Transaction;
import org.springframework.stereotype.Service;

@Service
public class CustomerTransactionApplication {
    private CustomerTransactionRepositoryInterface customerTransactionRepo;

    public CustomerTransactionApplication(
            CustomerTransactionRepositoryInterface customerTransactionRepositoryInterface
    ) {
        this.customerTransactionRepo = customerTransactionRepositoryInterface;
    }

    public Transaction referTransactionInfoById(int transactionId) {
        return customerTransactionRepo.getTransactionInfoById(transactionId);
    }

    public Customer referCustomerInfoById(int customerId) {
        return customerTransactionRepo.getCustomerInfoById(customerId);
    }
}
