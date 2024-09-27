package com.takata_kento.spring_multi_db_access.domain;

import java.sql.Date;

public record Transaction(
        int  transactionId,
        int  customerId,
        int  total,
        Date date
) {
}
