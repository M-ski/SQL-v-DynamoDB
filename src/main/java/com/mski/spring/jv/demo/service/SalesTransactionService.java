package com.mski.spring.jv.demo.service;

import com.mski.spring.jv.demo.model.Transaction;
import com.mski.spring.jv.demo.model.SoldItem;
import com.mski.spring.jv.demo.model.Stock;
import com.mski.spring.jv.demo.model.Receipt;
import com.mski.spring.jv.demo.repository.TransactionRepository;
import com.mski.spring.jv.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
public class SalesTransactionService {

    private final TransactionRepository transactionRepository;
    private final StockRepository stockRepository;

    public SalesTransactionService(TransactionRepository transactionRepository, StockRepository stockRepository) {
        this.transactionRepository = transactionRepository;
        this.stockRepository = stockRepository;
    }

    public void addItem(Transaction transaction, String stockName, Integer quantity) {
        Stock stock = stockRepository.findByName(stockName);
        if (stock == null) {
            throw new IllegalArgumentException(String.format("Stock [%s] does not exist", stockName));
        }

        boolean wasAddedInSitu = false;

        for(SoldItem soldItem : transaction.getSoldItemList()) {
            if (soldItem.getStock().equals(stock)) {
                soldItem.setQuantity(soldItem.getQuantity() + quantity);
                wasAddedInSitu = true;
            }
        }

        if (!wasAddedInSitu) {
            transaction.add(SoldItem.builder().stock(stock).quantity(quantity).build());
        }

        transactionRepository.save(transaction);
    }

    public Receipt checkout(Transaction transaction) {
        BigDecimal totalPrice = new BigDecimal(0);
        List<Receipt.BillableItem> billableItemList = new LinkedList<>();

        for (SoldItem soldItem : transaction.getSoldItemList()) {
            BigDecimal billableCost = soldItem.getStock().getPrice()
                    .multiply(BigDecimal.valueOf(soldItem.getQuantity()));

            billableItemList.add(new Receipt.BillableItem(
                    soldItem.getStock().getName(), soldItem.getQuantity(), billableCost
            ));

            totalPrice = totalPrice.add(billableCost);
        }

        return new Receipt(totalPrice, billableItemList);
    }
}
