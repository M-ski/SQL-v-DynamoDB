package com.mski.spring.jv.jdbc.model;

public record Customer(String id, String name, String email){
    public Customer(String name, String email) {
        this(null, name, email);
    }
}
