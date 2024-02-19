package com.mski.spring.jv.demo.service;

import com.mski.spring.jv.demo.model.Cart;
import com.mski.spring.jv.demo.model.CartItem;
import com.mski.spring.jv.demo.model.Stock;
import com.mski.spring.jv.demo.repository.CartRepository;
import com.mski.spring.jv.demo.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class DBPopulator {

    private final StockRepository stockRepository;
    private final CartRepository cartRepository;

    public DBPopulator(StockRepository stockRepository, CartRepository cartRepository) {
        this.stockRepository = stockRepository;
        this.cartRepository = cartRepository;
    }

    @PostConstruct
    public void init() {
        stockRepository.save(new Stock(null, "Milk", new HashSet<>(), BigDecimal.valueOf(1.5)));
        stockRepository.save(new Stock(null, "Wine", Set.of(Stock.Restrictions.ALCOHOL), BigDecimal.valueOf(5)));
        stockRepository.save(new Stock(null, "Smokey Wine", Set.of(Stock.Restrictions.TOBACCO, Stock.Restrictions.ALCOHOL), BigDecimal.valueOf(7)));

        Stock wine = stockRepository.findByName("Wine");
        Stock milk = stockRepository.findByName("Milk");
        log.info("Stock: {}", wine);
        log.info("Stock: {}", stockRepository.findByName("Smokey Wine"));

        Cart cart = new Cart();
        cart.add(CartItem.builder().stock(wine).quantity(1).build());
        cart.add(CartItem.builder().stock(milk).quantity(2).build());
        cartRepository.save(cart);

        List<Cart> allCarts = cartRepository.findAll();
        log.info("Carts: {}", allCarts);

        Cart firstCart = allCarts.getFirst();


    }
}
