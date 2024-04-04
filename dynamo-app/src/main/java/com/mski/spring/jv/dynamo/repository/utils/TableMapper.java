package com.mski.spring.jv.dynamo.repository.utils;

import com.mski.spring.jv.dynamo.model.Customer;
import com.mski.spring.jv.dynamo.model.ItemType;
import com.mski.spring.jv.dynamo.model.Sale;
import com.mski.spring.jv.dynamo.model.Stock;
import jakarta.annotation.PostConstruct;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.document.DocumentTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.document.EnhancedDocument;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.*;

import static com.mski.spring.jv.dynamo.model.BaseEntity.GSI_1_REVERSE_SORT_KEY;

@Component
public class TableMapper {

    public static final String STORE_TABLE = "STORE";
    public static final DocumentTableSchema TABLE_SCHEMA = TableSchema.documentSchemaBuilder()
            .addIndexPartitionKey(GSI_1_REVERSE_SORT_KEY, "SK", AttributeValueType.S)
            .addIndexSortKey(GSI_1_REVERSE_SORT_KEY, "SP", AttributeValueType.S)
            .build();

    public static final Map<Class<?>, ItemType> MAPPED_CLASSES = new HashMap<>();
    static {
        MAPPED_CLASSES.put(Customer.class, ItemType.CUSTOMER);
        MAPPED_CLASSES.put(Sale.class, ItemType.SALE);
        MAPPED_CLASSES.put(Stock.class, ItemType.STOCK);
    };
    private final Map<Class<?>, TableSchema<?>> mappers;

    public TableMapper() {
        mappers = new HashMap<>();
        for (Class<?> clazz: MAPPED_CLASSES.keySet()) {
            BeanTableSchema<?> value = TableSchema.fromBean(clazz);
            this.mappers.put(clazz, value);
        }
    }

    public Map<ItemType, Collection<EnhancedDocument>> documentsByItemType(Collection<EnhancedDocument> documents) {
        Map<ItemType, Collection<EnhancedDocument>> itemMap = new EnumMap<>(ItemType.class);
        for(EnhancedDocument doc: documents) {
            itemMap.merge(
                    ItemType.valueOf(doc.getString("itemType")),
                    Lists.newArrayList(doc),
                    (docs1, docs2) -> {
                        docs1.addAll(docs2);
                        return docs1;
                    }
            );
        }
        return itemMap;
    }

    public <T> T map(EnhancedDocument document, Class<T> expectedClass) {
        TableSchema<?> tableSchema = this.mappers.get(expectedClass);
        if (tableSchema == null) {
            throw new IllegalArgumentException("No Mapper found for class %s".formatted(expectedClass.getSimpleName()));
        }
        return expectedClass.cast(tableSchema.mapToItem(document.toMap()));
    }

    public <T> T map(Collection<EnhancedDocument> document, Class<T> expectedClass) {
        return map(document.iterator().next(), expectedClass);
    }

    public <T> List<T> mapItems(Collection<EnhancedDocument> documents, Class<T> expectedClass) {
        List<T> items = new LinkedList<>();
        for(EnhancedDocument document: documents) {
            items.add(map(document, expectedClass));
        }
        return items;
    }
}
