package com.mski.spring.jv.jdbc;

import com.mski.spring.jv.jdbc.model.Currency;
import com.mski.spring.jv.jdbc.model.Customer;
import com.mski.spring.jv.jdbc.model.MinorUnits;
import com.mski.spring.jv.jdbc.model.SoldItem;
import com.mski.spring.jv.jdbc.model.dto.SaleTransactionDTO;
import com.mski.spring.jv.jdbc.repository.SalesRepository;
import com.mski.spring.jv.jdbc.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Service
public class SalesTransactionService {

    private final SalesRepository salesRepository;

    private final StockRepository stockRepository;

    public SalesTransactionService(SalesRepository salesRepository, StockRepository stockRepository) {
        this.salesRepository = salesRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void sellItems(Customer customer, Collection<SoldItem> items) {

        int totalMinorUnits = 0;
        for (SoldItem soldItem : items) {dd
            totalMinorUnits += soldItem.quantity() * soldItem.stock().price().amountInMinorUnits();
        }

        SaleTransactionDTO saleTransactionDTO = new SaleTransactionDTO(
                UUID.randomUUID().toString(),
                customer.id(),
                LocalDateTime.now(),
                new MinorUnits(totalMinorUnits, Currency.GBP),
                items
        );

        // try to decrease stock first before confirming the sale - in case we run out of something
        stockRepository.decreaseQuantities(items);
        // then save the new sale
        salesRepository.saveTransaction(saleTransactionDTO);
    }
}
