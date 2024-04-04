package com.mski.spring.jv.dynamo.model.dto;

import com.mski.spring.jv.dynamo.model.Sale;
import com.mski.spring.jv.dynamo.model.Stock;

import java.util.Collection;

public record SaleDTO(Sale sale, Collection<Stock> purchasedItems) {
}
