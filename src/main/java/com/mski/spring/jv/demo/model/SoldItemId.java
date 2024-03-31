package com.mski.spring.jv.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class SoldItemId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private UUID stockId;

    @ManyToOne
    @JoinColumn(name = "sold_item_id")
    private UUID soldItemId;
}
