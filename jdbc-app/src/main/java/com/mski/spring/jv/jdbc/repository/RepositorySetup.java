package com.mski.spring.jv.jdbc.repository;

import com.mski.spring.jv.jdbc.SalesTransactionService;
import com.mski.spring.jv.jdbc.model.*;
import com.mski.spring.jv.jdbc.model.dto.TransactionDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepositorySetup {

    @Value("classpath:db_destruct.sql")
    private Resource tearDownCommands;

    @Value("classpath:db_init.sql")
    private Resource dbInitCommands;

    private final UserRepository userRepository;
    private final StockRepository stockRepository;
    private final SalesTransactionService salesTransactionService;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RepositorySetup(UserRepository userRepository, StockRepository stockRepository,
                           SalesTransactionService salesTransactionService, NamedParameterJdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.salesTransactionService = salesTransactionService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() throws SQLException {
        getExecuteSqlScript(tearDownCommands);
        getExecuteSqlScript(dbInitCommands);

        Customer bob = userRepository.save(new Customer("Bob", "bob@gmail.com"));
        Customer Jane = userRepository.save(new Customer("Jane", "Jane@gmail.com"));
        Customer alex = userRepository.save(new Customer("Alex", "Alex@yahoo.com"));

        Stock milk = stockRepository.save(new Stock(null, "Milk", new MinorUnits(110, Currency.GBP), 5));
        Stock eggs = stockRepository.save(new Stock(null, "Eggs", new MinorUnits(135, Currency.GBP), 3));
        Stock sugar = stockRepository.save(new Stock(null, "Sugar", new MinorUnits(89, Currency.GBP), 7));
        Stock wine = stockRepository.save(new Stock(null, "Wine", new MinorUnits(550, Currency.GBP), 2));

        salesTransactionService.sellItems(bob, List.of(new SoldItem(milk, 1), new SoldItem(sugar, 2)));
        salesTransactionService.sellItems(alex, List.of(new SoldItem(milk, 1), new SoldItem(eggs, 1)));
    }

    private void getExecuteSqlScript(Resource tearDownCommands) throws SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getJdbcTemplate().getDataSource().getConnection(), tearDownCommands);
    }

}
