package com.mski.spring.jv.jdbc.repository;

import com.mski.spring.jv.jdbc.model.Currency;
import com.mski.spring.jv.jdbc.model.MinorUnits;
import com.mski.spring.jv.jdbc.model.SoldItem;
import com.mski.spring.jv.jdbc.model.Stock;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StockRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StockRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Stock save(Stock stock) {
        String stockId = stock.id() == null ? UUID.randomUUID().toString() : stock.id();
        jdbcTemplate.update("""
                INSERT INTO stock VALUES (:id, :name, :price_minor_units, :price_ccy, :quantity)
                ON DUPLICATE KEY UPDATE name=:name, price_minor_units=:price_minor_units, price_ccy=:price_ccy, quantity=:quantity;
                """, Map.of(
                "id", stockId,
                "name", stock.itemName(),
                "price_minor_units", stock.price().amountInMinorUnits(),
                "price_ccy", stock.price().currency().name(),
                "quantity", stock.quantityRemaining())
        );
        return new Stock(stockId, stock.itemName(), stock.price(), stock.quantityRemaining());
    }

    public void decreaseQuantities(Collection<SoldItem> soldItems) {
        Map<String, Object>[] updates = new Map[soldItems.size()];

        Iterator<SoldItem> iterator = soldItems.iterator();
        for(int i = 0; i < soldItems.size(); i++) {
            SoldItem item = iterator.next();
            updates[i] = Map.of(
                    "id", item.stock().id(),
                    "amount", item.quantity()
            );
        }

        jdbcTemplate.batchUpdate("UPDATE stock SET quantity = quantity - :amount where id=:id", updates);
    }

    public Stock findByName(String name) {
        return jdbcTemplate.query("""
                        SELECT id, name, price_minor_units, price_ccy, quantity
                        FROM customers WHERE name=:name""",
                Map.of("name", name),
                (ResultSetExtractor<Stock>) rs ->
                        new Stock(
                                rs.getString("id"),
                                rs.getString("name"),
                                new MinorUnits(rs.getInt("price_minor_units"), Currency.forValue(rs.getString("price_ccy"))),
                                rs.getInt("quantity")
                        )
        );
    }
}
