package com.mski.spring.jv.jdbc;

import com.mski.spring.jv.jdbc.model.Currency;
import com.mski.spring.jv.jdbc.model.Customer;
import com.mski.spring.jv.jdbc.model.MinorUnits;
import com.mski.spring.jv.jdbc.model.SoldItem;
import com.mski.spring.jv.jdbc.model.dto.TransactionDTO;
import com.mski.spring.jv.jdbc.repository.SalesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class SalesTransactionService {

    private final SalesRepository salesRepository;

    public SalesTransactionService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public TransactionDTO sellItems(Customer customer, Collection<SoldItem> items) {
        int totalMinorUnits = 0;
        for (SoldItem soldItem : items) {
            totalMinorUnits += soldItem.quantity() * soldItem.stock().price().amountInMinorUnits();
        }

        TransactionDTO transactionDTO = new TransactionDTO(
                customer.id(),
                LocalDateTime.now(),
                new MinorUnits(totalMinorUnits, Currency.GBP),
                items
        );

        salesRepository.saveTransaction(transactionDTO);

        return transactionDTO;
    }
}
