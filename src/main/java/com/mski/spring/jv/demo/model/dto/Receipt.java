package com.mski.spring.jv.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {

    private BigDecimal totalCost;
    private List<BillableItem> billableItems;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BillableItem {
        private String itemName;
        private Integer quantity;
        private BigDecimal purchasePrice;
    }
}
