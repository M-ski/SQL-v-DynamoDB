package com.mski.spring.jv.dynamo.repository;

import com.mski.spring.jv.dynamo.model.BaseEntity;
import com.mski.spring.jv.dynamo.model.ItemType;
import com.mski.spring.jv.dynamo.model.Sale;
import com.mski.spring.jv.dynamo.model.Stock;
import com.mski.spring.jv.dynamo.model.dto.SaleDTO;
import com.mski.spring.jv.dynamo.repository.utils.TableMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.document.EnhancedDocument;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.TransactWriteItem;
import software.amazon.awssdk.services.dynamodb.model.Update;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mski.spring.jv.dynamo.repository.utils.TableMapper.STORE_TABLE;
import static com.mski.spring.jv.dynamo.repository.utils.TableMapper.TABLE_SCHEMA;
import static com.mski.spring.jv.dynamo.repository.utils.TransactionWriteUtils.addTransactWriteItem;

@Slf4j
@Repository
public class SaleRepository {

    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final TableMapper tableMapper;
    private DynamoDbTable<EnhancedDocument> table;
    private DynamoDbTable<Sale> saleTable;
    private DynamoDbTable<Stock> stockTable;

    public SaleRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, TableMapper tableMapper) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.tableMapper = tableMapper;
    }

    @PostConstruct
    public void init() {
        this.table = dynamoDbEnhancedClient.table(STORE_TABLE, TABLE_SCHEMA);
        this.stockTable = dynamoDbEnhancedClient.table(STORE_TABLE, TableSchema.fromBean(Stock.class));
        this.saleTable = dynamoDbEnhancedClient.table(STORE_TABLE, TableSchema.fromBean(Sale.class));
    }

    public void saveSaleWithStockChecking(SaleDTO transaction) {
        // first inititalise our request with the sale item, and the sold stock items
        TransactWriteItemsEnhancedRequest.Builder requestBuilder = TransactWriteItemsEnhancedRequest.builder().addPutItem(saleTable, transaction.sale());
        transaction.purchasedItems().forEach(item -> requestBuilder.addPutItem(stockTable, item.toBuilder().SK(transaction.sale().getSK()).build()));

        // then add our updates to the stock, to attempt to reduce down the quantity of stock by what was sold
        transaction.purchasedItems().forEach(item -> addTransactWriteItem(requestBuilder, TransactWriteItem.builder()
                .update(Update.builder()
                        .key(Map.of(
                                "PK", AttributeValue.fromS(item.getPK()),
                                "SK", AttributeValue.fromS(item.getPK())
                        ))
                        .tableName(STORE_TABLE)
                        .updateExpression("SET quantity = quantity - :val")
                        .conditionExpression("quantity >= :val")
                        .expressionAttributeValues(Map.of(":val", AttributeValue.fromN(item.getQuantity().toString())))
                        .build())
                .build()));

        TransactWriteItemsEnhancedRequest build = requestBuilder.build();

        log.info("Transact write: {}", build.transactWriteItems());

        dynamoDbEnhancedClient.transactWriteItems(build);
    }

    public SaleDTO getSale(String saleId) {
        // first, query our table with the PK of the customer and return the data as a List of items
        List<EnhancedDocument> items = this.table.index(BaseEntity.GSI_1_REVERSE_SORT_KEY).query(
                QueryConditional.keyEqualTo(
                        Key.builder().partitionValue(saleId).build()
                )).stream().flatMap(page -> page.items().stream()).collect(Collectors.toList());

        // then map these items by ItemType
        Map<ItemType, Collection<EnhancedDocument>> itemsByType = tableMapper.documentsByItemType(items);

        // Convert back each DB Item into 1->many items defined in our model
        Sale sale = tableMapper.map(itemsByType.get(ItemType.SALE), Sale.class);
        List<Stock> purchasedItems = tableMapper.mapItems(itemsByType.get(ItemType.STOCK), Stock.class);

        return new SaleDTO(sale, purchasedItems);
    }
}
