package com.mski.spring.jv.demo;

import com.mski.spring.jv.demo.model.Cart;
import com.mski.spring.jv.demo.repository.CartRepository;
import com.mski.spring.jv.demo.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("run Hook");
        Cart cart = cartRepository.findAll().getFirst();

        log.info("Cart before add {}", cart);

        cartService.addItem(cart, "Milk", 1);

        cart = cartRepository.findAll().getFirst();
        log.info("Cart after add {}", cart);
    }
}
