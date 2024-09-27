package com.takata_kento.spring_multi_db_access.presentation;

import com.takata_kento.spring_multi_db_access.domain.Customer;
import com.takata_kento.spring_multi_db_access.domain.CustomerTransactionRepositoryInterface;
import com.takata_kento.spring_multi_db_access.domain.Transaction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringMultiDbAccessController {
    private CustomerTransactionRepositoryInterface customerTransactionRepo;

    public SpringMultiDbAccessController(
            CustomerTransactionRepositoryInterface customerTransactionRepositoryInterface
    ) {
        this.customerTransactionRepo = customerTransactionRepositoryInterface;
    }

    @GetMapping("/getTransactionInfo/{transactionId}")
    public Transaction getTransactionInfo(
            @PathVariable("transactionId") int transactionId
    ) {
        return customerTransactionRepo.getTransactionInfoById(transactionId);
    }

    @GetMapping("/getCustomerInfo/{customerId}")
    public Customer getCustomerInfo(
        @PathVariable("customerId") int customerId
    ) {
        return customerTransactionRepo.getCustomerInfoById(customerId);
    }
}
