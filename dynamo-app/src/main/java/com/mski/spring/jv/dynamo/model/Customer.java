package com.mski.spring.jv.dynamo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;


@Data
@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {

    @Getter(onMethod_ = {@DynamoDBAttribute(attributeName = "name")})
    private String name;

    @Getter(onMethod_ = {@DynamoDBAttribute(attributeName = "email")})
    private String email;

    @Override
    @DynamoDBAttribute(attributeName = "itemType")
    public ItemType getItemType() {
        return ItemType.CUSTOMER;
    }
}
