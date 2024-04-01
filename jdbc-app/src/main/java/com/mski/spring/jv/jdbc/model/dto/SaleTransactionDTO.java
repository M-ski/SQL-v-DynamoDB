package com.mski.spring.jv.jdbc.model.dto;

import com.mski.spring.jv.jdbc.model.MinorUnits;
import com.mski.spring.jv.jdbc.model.SoldItem;

import java.time.LocalDateTime;
import java.util.Collection;

public record SaleTransactionDTO(
        String id,
        String customerId,
        LocalDateTime when,
        MinorUnits total,
        Collection<SoldItem> items
) {

}

