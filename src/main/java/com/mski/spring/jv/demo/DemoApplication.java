package com.mski.spring.jv.demo;

import com.mski.spring.jv.demo.model.Stock;
import com.mski.spring.jv.demo.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private StockRepository stockRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("run Hook");
		stockRepository.save(new Stock(null, "Milk", new HashSet<>()));
		stockRepository.save(new Stock(null, "Wine", Set.of(Stock.Restrictions.ALCOHOL)));
		stockRepository.save(new Stock(null, "Smokey Wine", Set.of(Stock.Restrictions.TOBACCO, Stock.Restrictions.ALCOHOL)));

		log.info("Stock: {}", stockRepository.findByName("Wine"));
		log.info("Stock: {}", stockRepository.findByName("Smokey Wine"));
	}
}
