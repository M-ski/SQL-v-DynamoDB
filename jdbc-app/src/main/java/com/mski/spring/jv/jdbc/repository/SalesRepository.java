package com.mski.spring.jv.jdbc.repository;

import com.mski.spring.jv.jdbc.model.SoldItem;
import com.mski.spring.jv.jdbc.model.dto.SaleTransactionDTO;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
public class SalesRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SalesRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void saveTransaction(SaleTransactionDTO saleTransactionDTO) {
        jdbcTemplate.update("""
                INSERT INTO sales VALUES (:id, :customer_id, :when, :total_price_minor_units, :total_price_minor_units)
                ON DUPLICATE KEY UPDATE customer_id=:customer_id, `when`=:when,
                                        total_price_minor_units=:total_price_minor_units, total_price_ccy=:total_price_ccy
                """, Map.of(
                "id", saleTransactionDTO.id(),
                "customer_id", saleTransactionDTO.customerId(),
                "when", saleTransactionDTO.when(),
                "total_price_minor_units", saleTransactionDTO.total().amountInMinorUnits(),
                "total_price_ccy", saleTransactionDTO.total().currency().name())
        );

        Map<String, Object>[] soldItemsValues = new Map[saleTransactionDTO.items().size()];
        int tracker = 0;

        for (SoldItem item : saleTransactionDTO.items()) {
            soldItemsValues[tracker++] = Map.of(
                    "sale_id", saleTransactionDTO.id(),
                    "stock_id", item.stock().id(),
                    "quantity", item.quantity()
            );
        }

        jdbcTemplate.batchUpdate("""
                INSERT INTO sold_items VALUES(:sale_id, :stock_id, :quantity)
                ON DUPLICATE KEY UPDATE sale_id=:sale_id, stock_id=:stock_id, quantity=:quantity
                """, soldItemsValues);
    }

//    public Collection<TransactionDTO> transactionsForCustomer(String customerId) {
//        jdbcTemplate.query("""
//                SELECT customer_id, `when`, stock_id, quantity, total_price_minor_units, total_price_minor_units
//                """)
//    }
}
