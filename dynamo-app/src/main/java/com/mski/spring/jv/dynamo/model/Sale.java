package com.mski.spring.jv.dynamo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseEntity {

    @Getter(onMethod_ = {@DynamoDBAttribute(attributeName = "when")})
    private LocalDateTime when;

    @Getter(onMethod_ = {@DynamoDBAttribute(attributeName = "totalPrice")})
    private Double totalPrice;

    @Override
    @DynamoDBAttribute(attributeName = "itemType")
    public ItemType getItemType() {
        return ItemType.SALE;
    }

    public static Sale forCustomer(Customer customer, LocalDateTime when, Double totalPrice) {
        Sale sale = new Sale(when, totalPrice);
        sale.setPK(customer.getPK());
        sale.setSK(ItemType.SALE + "#" + UUID.randomUUID());
        return sale;
    }
}
