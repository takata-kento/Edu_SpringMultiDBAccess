package com.takata_kento.spring_multi_db_access.domain;

import java.sql.Date;

public record Customer(
        int    customerId,
        String name,
        int    age,
        Date   birthday,
        String gender
) {
}
