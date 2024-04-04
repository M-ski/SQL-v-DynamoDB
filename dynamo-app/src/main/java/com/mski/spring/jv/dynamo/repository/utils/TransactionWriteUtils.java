package com.mski.spring.jv.dynamo.repository.utils;

import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.TransactWriteItem;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Supplier;

public class TransactionWriteUtils {

    private static Field itemSupplierList;

    @SneakyThrows
    public static void addTransactWriteItem(TransactWriteItemsEnhancedRequest.Builder requestBuilder, TransactWriteItem transactWriteItem) {
        if(itemSupplierList == null) {
            itemSupplierList = ReflectionUtils.findField(
                    TransactWriteItemsEnhancedRequest.Builder.class, "itemSupplierList"
            );
            ReflectionUtils.makeAccessible(itemSupplierList);
        }
        ((List<Supplier<TransactWriteItem>>) itemSupplierList.get(requestBuilder)).add(() -> transactWriteItem);
    }

}
