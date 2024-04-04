package com.mski.spring.jv.dynamo.repository;

import com.mski.spring.jv.dynamo.model.Customer;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.UUID;

import static com.mski.spring.jv.dynamo.repository.utils.TableMapper.STORE_TABLE;

@Repository
public class CustomerRepository {

    public static final String HASH = "#";
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private DynamoDbTable<Customer> customerTable;


    public CustomerRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void init() {
        this.customerTable = dynamoDbEnhancedClient.table(STORE_TABLE, TableSchema.fromBean(Customer.class));
    }

    public Customer save(Customer customer) {
        if(customer.getPK() == null) {
            customer.setPK(customer.getItemType() + HASH + UUID.randomUUID());
            customer.setSK(customer.getPK());
        }
        this.customerTable.putItem(customer);
        return customer;
    }
}
