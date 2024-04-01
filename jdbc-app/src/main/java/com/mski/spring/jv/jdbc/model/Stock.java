package com.mski.spring.jv.jdbc.model;

public record Stock(
        String id, String itemName, MinorUnits price, int quantityRemaining
) { }
