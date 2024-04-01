package com.mski.spring.jv.demo.jpa;

import com.mski.spring.jv.demo.jpa.model.Transaction;
import com.mski.spring.jv.demo.jpa.repository.TransactionRepository;
import com.mski.spring.jv.demo.jpa.service.SalesTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JpaApp implements CommandLineRunner {

    @Autowired
    private SalesTransactionService salesTransactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("run Hook");
        Transaction transaction = transactionRepository.findAll().getFirst();

        log.info("Cart before add {}", transaction);

        salesTransactionService.addItem(transaction, "Milk", 1);

        transaction = transactionRepository.findAll().getFirst();
        log.info("Cart after add {}", transaction);
    }
}
