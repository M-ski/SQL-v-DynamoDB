package com.mski.spring.jv.dynamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DynamoDBApp {

    public static void main(String[] args) {
        SpringApplication.run(DynamoDBApp.class, args);
    }

}