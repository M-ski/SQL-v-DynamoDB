package com.mski.spring.jv.dynamo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.*;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

    public static final String GSI_1_REVERSE_SORT_KEY = "GSI_R";

    @Getter(onMethod_ = {@DynamoDbPartitionKey, @DynamoDBAttribute(attributeName = "PK"), @DynamoDbSecondarySortKey(indexNames = GSI_1_REVERSE_SORT_KEY)})
    private String PK;

    @Getter(onMethod_ = {@DynamoDbSortKey, @DynamoDBAttribute(attributeName = "SK"), @DynamoDbSecondaryPartitionKey(indexNames = GSI_1_REVERSE_SORT_KEY)})
    private String SK;

    public abstract ItemType getItemType();

    public void setItemType(ItemType itemType) {
        // ignore, required by the dynamoSDK
    }

}
