package com.mski.spring.jv.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class JdbcApp {
    public static void main(String[] args) {
        SpringApplication.run(JdbcApp.class, args);
    }
}