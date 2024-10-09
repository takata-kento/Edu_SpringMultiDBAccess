package com.takata_kento.spring_multi_db_access.presentation;

import com.takata_kento.spring_multi_db_access.application.CustomerTransactionApplication;
import com.takata_kento.spring_multi_db_access.domain.Customer;
import com.takata_kento.spring_multi_db_access.domain.CustomerTransactionRepositoryInterface;
import com.takata_kento.spring_multi_db_access.domain.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringMultiDbAccessController {
    private CustomerTransactionApplication customerTransactionApplication;

    public SpringMultiDbAccessController(
            CustomerTransactionApplication customerTransactionRepositoryInterface
    ) {
        this.customerTransactionApplication = customerTransactionRepositoryInterface;
    }

    @GetMapping("/getTransactionInfo/{transactionId}")
    public Transaction getTransactionInfo(
            @PathVariable("transactionId") int transactionId
    ) {
        return customerTransactionApplication.referTransactionInfoById(transactionId);
    }

    @GetMapping("/getCustomerInfo/{customerId}")
    public Customer getCustomerInfo(
        @PathVariable("customerId") int customerId
    ) {
        return customerTransactionApplication.referCustomerInfoById(customerId);
    }
}
