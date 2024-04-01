package com.mski.spring.jv.jdbc.repository;

import com.mski.spring.jv.jdbc.model.Customer;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Customer save(Customer customer) {
        String userId = customer.id() == null ? UUID.randomUUID().toString() : customer.id();
        jdbcTemplate.update("""
                INSERT INTO customers VALUES (:id, :name, :email)
                ON DUPLICATE KEY UPDATE name=:name, email=:email;
                """, Map.of(
                "id", userId,
                "name", customer.name(),
                "email", customer.email())
        );
        return new Customer(userId, customer.name(), customer.email());
    }

    public Customer findByName(String name) {
        return jdbcTemplate.query("SELECT name, email FROM customers WHERE name=:name",
                Map.of("name", name),
                (ResultSetExtractor<Customer>) rs ->
                        new Customer(rs.getString("name"), rs.getString("email"))
        );
    }
}
