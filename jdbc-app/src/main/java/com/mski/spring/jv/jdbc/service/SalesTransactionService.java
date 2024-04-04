package com.mski.spring.jv.jdbc.service;

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
        // get the total purchase cost of the items
        int totalMinorUnits = getTotalPurchaseCost(items);
        // construct a sale object (a sale with multiple purchased items)
        SaleTransactionDTO saleTransactionDTO = getSaleTransactionDTO(customer, items, totalMinorUnits);

        // then try to decrease stock first before confirming the sale - in case we run out of something
        stockRepository.decreaseQuantities(items);
        // finally save the new sale
        salesRepository.saveTransaction(saleTransactionDTO);
    }

    private static int getTotalPurchaseCost(Collection<SoldItem> items) {
        int totalMinorUnits = 0;
        for (SoldItem soldItem : items) {
            totalMinorUnits += soldItem.quantity() * soldItem.stock().price().amountInMinorUnits();
        }
        return totalMinorUnits;
    }

    private static SaleTransactionDTO getSaleTransactionDTO(Customer customer, Collection<SoldItem> items, int totalMinorUnits) {
        return new SaleTransactionDTO(
                UUID.randomUUID().toString(),
                customer.id(),
                LocalDateTime.now(),
                new MinorUnits(totalMinorUnits, Currency.GBP),
                items
        );
    }
}
