package com.mski.spring.jv.demo.service;

import com.mski.spring.jv.demo.model.Transaction;
import com.mski.spring.jv.demo.model.SoldItem;
import com.mski.spring.jv.demo.model.Stock;
import com.mski.spring.jv.demo.model.Customer;
import com.mski.spring.jv.demo.repository.TransactionRepository;
import com.mski.spring.jv.demo.repository.StockRepository;
import com.mski.spring.jv.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class DBPopulator {

    private final StockRepository stockRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DBPopulator(StockRepository stockRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.stockRepository = stockRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Customer bob = userRepository.save(Customer.builder().name("Bob").build());
        Customer jane = userRepository.save(Customer.builder().name("Jane").build());
        Customer alex = userRepository.save(Customer.builder().name("Alex").build());

        log.info("All users [{}]", userRepository.findAll());

        stockRepository.save(new Stock(null, "Milk", new HashSet<>(), BigDecimal.valueOf(1.10), 5));
        stockRepository.save(new Stock(null, "Eggs", new HashSet<>(), BigDecimal.valueOf(5), 3));
        stockRepository.save(new Stock(null, "Sugar", new HashSet<>(), BigDecimal.valueOf(5), 7));
        stockRepository.save(new Stock(null, "Wine", Set.of(Stock.Restrictions.ALCOHOL), BigDecimal.valueOf(5.50), 2));

        Stock milk = stockRepository.findByName("Milk");
        Stock eggs = stockRepository.findByName("Eggs");
        Stock sugar = stockRepository.findByName("Sugar");
        Stock wine = stockRepository.findByName("Wine");
        log.info("Stock: {} | {} | {} | {}", milk, eggs, sugar, wine);

        Transaction bobTransaction = new Transaction();
        bobTransaction.add(SoldItem.builder().stock(milk).quantity(1).build());
        bobTransaction.add(SoldItem.builder().stock(sugar).quantity(2).build());
        bobTransaction.setCustomer(bob);
        transactionRepository.save(bobTransaction);
        log.info("Bob's cart: {}", bobTransaction);

        Transaction alexTransaction = new Transaction();
        alexTransaction.add(SoldItem.builder().stock(milk).quantity(1).build());
        alexTransaction.add(SoldItem.builder().stock(eggs).quantity(1).build());
        alexTransaction.setCustomer(alex);
        transactionRepository.save(alexTransaction);
        log.info("Alex's cart: {}", alexTransaction);

        List<Transaction> allTransactions = transactionRepository.findAll();
        log.info("Carts: {}", allTransactions);
        log.info("All users after adding carts [{}]", userRepository.findAll());

    }
}
