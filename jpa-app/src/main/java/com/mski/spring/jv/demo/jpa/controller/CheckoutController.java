package com.mski.spring.jv.demo.jpa.controller;

import com.mski.spring.jv.demo.jpa.model.Receipt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/stock/")
public class CheckoutController {

    @GetMapping("v1/checkout/{cartID}")
    public Receipt checkout(@PathVariable("cartID") String cartID) {
        return null;
    }
}
