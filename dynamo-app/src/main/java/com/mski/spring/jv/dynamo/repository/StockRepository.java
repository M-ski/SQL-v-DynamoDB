package com.mski.spring.jv.dynamo.repository;

import com.mski.spring.jv.dynamo.model.Stock;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

import static com.mski.spring.jv.dynamo.repository.CustomerRepository.HASH;
import static com.mski.spring.jv.dynamo.repository.utils.TableMapper.STORE_TABLE;

@Repository
public class StockRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private DynamoDbTable<Stock> customerTable;


    public StockRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void init() {
        this.customerTable = dynamoDbEnhancedClient.table(STORE_TABLE, TableSchema.fromBean(Stock.class));
    }

    public Stock save(Stock stock) {
        if (stock.getPK() == null) {
            stock.setPK(stock.getItemType() + HASH + UUID.randomUUID());
            stock.setSK(stock.getPK());
        }
        this.customerTable.updateItem(stock);
        return stock;
    }
}
