package com.mski.spring.jv.jdbc.model;

public enum Currency {
    GBP, USD, EUR, PLN;


    public static Currency forValue(String ccy) {
        for(Currency currency : Currency.values()) {
            if (currency.toString().equals(ccy)) {
                return currency;
            }
        }
        throw new IllegalArgumentException(String.format("Currency [%s] does not exist!", ccy));
    }
}
