package com.mski.spring.jv.demo.service;

import com.mski.spring.jv.demo.model.Cart;
import com.mski.spring.jv.demo.model.CartItem;
import com.mski.spring.jv.demo.model.Stock;
import com.mski.spring.jv.demo.model.dto.Receipt;
import com.mski.spring.jv.demo.repository.CartRepository;
import com.mski.spring.jv.demo.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final StockRepository stockRepository;

    public CartService(CartRepository cartRepository, StockRepository stockRepository) {
        this.cartRepository = cartRepository;
        this.stockRepository = stockRepository;
    }

    public void addItem(Cart cart, String stockName, Integer quantity) {
        Stock stock = stockRepository.findByName(stockName);
        if (stock == null) {
            throw new IllegalArgumentException(String.format("Stock [%s] does not exist", stockName));
        }

        boolean wasAddedInSitu = false;

        for(CartItem cartItem: cart.getCartItemList()) {
            if (cartItem.getStock().equals(stock)) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                wasAddedInSitu = true;
            }
        }

        if (!wasAddedInSitu) {
            cart.add(CartItem.builder().stock(stock).quantity(quantity).build());
        }

        cartRepository.save(cart);
    }

    public Receipt checkout(Cart cart) {
        BigDecimal totalPrice = new BigDecimal(0);
        List<Receipt.BillableItem> billableItemList = new LinkedList<>();

        for (CartItem cartItem : cart.getCartItemList()) {
            BigDecimal billableCost = cartItem.getStock().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            billableItemList.add(new Receipt.BillableItem(
                    cartItem.getStock().getName(), cartItem.getQuantity(), billableCost
            ));

            totalPrice = totalPrice.add(billableCost);
        }

        return new Receipt(totalPrice, billableItemList);
    }
}
