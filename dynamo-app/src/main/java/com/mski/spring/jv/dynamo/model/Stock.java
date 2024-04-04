package com.mski.spring.jv.dynamo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.*;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends BaseEntity {

    @Getter(onMethod_ = {@DynamoDBAttribute(attributeName = "name")})
    private String name;

    @Getter(onMethod_ = {@DynamoDBAttribute(attributeName = "price")})
    private Double price;

    @Getter(onMethod_ = {@DynamoDBAttribute(attributeName = "quantity")})
    private Integer quantity;

    @Override
    @DynamoDBAttribute(attributeName = "itemType")
    public ItemType getItemType() {
        return ItemType.STOCK;
    }

}
