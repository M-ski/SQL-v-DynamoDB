package com.mski.spring.jv.jdbc.model.dto;

import com.mski.spring.jv.jdbc.model.MinorUnits;
import com.mski.spring.jv.jdbc.model.SoldItem;
import com.mski.spring.jv.jdbc.model.Stock;

import java.time.LocalDateTime;
import java.util.Collection;

public record TransactionDTO(
        String customerId,
        LocalDateTime when,
        MinorUnits total,
        Collection<SoldItem> items
) {

}

