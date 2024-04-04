package com.mski.spring.jv.dynamo.service;

import com.mski.spring.jv.dynamo.model.Customer;
import com.mski.spring.jv.dynamo.model.Sale;
import com.mski.spring.jv.dynamo.model.Stock;
import com.mski.spring.jv.dynamo.model.dto.SaleDTO;
import com.mski.spring.jv.dynamo.repository.CustomerRepository;
import com.mski.spring.jv.dynamo.repository.SaleRepository;
import com.mski.spring.jv.dynamo.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DescribeTableEnhancedResponse;

import java.time.LocalDateTime;
import java.util.List;

import static com.mski.spring.jv.dynamo.repository.utils.TableMapper.STORE_TABLE;

@Slf4j
@Component
public class DBPopulator {

    private final StockRepository stockRepository;
    private final SaleRepository saleRepository;
    private final CustomerRepository userRepository;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public DBPopulator(StockRepository stockRepository,
                       SaleRepository transactionRepository,
                       CustomerRepository userRepository,
                       DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.stockRepository = stockRepository;
        this.saleRepository = transactionRepository;
        this.userRepository = userRepository;
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void init() {
        DynamoDbTable<Sale> table = dynamoDbEnhancedClient.table(STORE_TABLE, TableSchema.fromBean(Sale.class));
        try {
            DescribeTableEnhancedResponse resp = table.describeTable();
        } catch (Exception e) {
            table.createTable();
        }

        Customer bob = userRepository.save(new Customer("Bob", "bob@gmail.com"));
        Customer jane = userRepository.save(new Customer("Jane", "Jane@gmail.com"));
        Customer alex = userRepository.save(new Customer("Alex", "Alex@yahoo.com"));

        Stock milk = stockRepository.save(new Stock("Milk", 1.10, 5));
        Stock eggs = stockRepository.save(new Stock("Eggs", 1.35, 3));
        Stock sugar = stockRepository.save(new Stock("Sugar", 0.89, 7));
        Stock wine = stockRepository.save(new Stock("Wine", 5.50, 2));
        log.info("Stock: {} | {} | {} | {}", milk, eggs, sugar, wine);

        Sale sale = Sale.forCustomer(bob, LocalDateTime.now(), 2.89);
        SaleDTO bobTransaction = new SaleDTO(sale,
                List.of(milk.toBuilder().quantity(1).build(), sugar.toBuilder().quantity(2).build())
        );

        saleRepository.saveSaleWithStockChecking(bobTransaction);
        SaleDTO bobsSaleFromTheDB = saleRepository.getSale(bobTransaction.sale().getSK());

        log.info("Bob's sale: {}", bobTransaction);
        log.info("Bob's sale from the DB: {}", bobsSaleFromTheDB);

        SaleDTO alexTransaction = new SaleDTO(
                Sale.forCustomer(alex, LocalDateTime.now(), 2.89),
                List.of(milk.toBuilder().quantity(1).build(), eggs.toBuilder().quantity(1).build())
        );

        saleRepository.saveSaleWithStockChecking(alexTransaction);
        SaleDTO alexsSaleFromTheDB = saleRepository.getSale(alexTransaction.sale().getSK());

        log.info("Alex's sale: {}", alexTransaction);
        log.info("Alex's sale from the DB: {}", alexsSaleFromTheDB);
    }
}
